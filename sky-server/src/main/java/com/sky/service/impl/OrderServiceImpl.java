package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.*;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper detailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private WebSocketServer socketServer;

    /**
     * 用户下单
     * @param submitDTO
     * @return
     */
    @Transactional
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO submitDTO) {

        //处理业务异常
        Long addressBookId = submitDTO.getAddressBookId();
        Long userId = BaseContext.getCurrentId();

        AddressBook addressBook = addressBookMapper.queryById(userId,addressBookId);

        //查询用户地址是否为空
        if(addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //查询用户购物车是否为空
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> cartRecords = shoppingCartMapper.query(shoppingCart);

        if(cartRecords == null || cartRecords.isEmpty()){
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(submitDTO,orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressBook.getDetail());
        orders.setRemark(submitDTO.getRemark());
        orders.setUserId(userId);

        orderMapper.insert(orders);

        //向订单明细表插入多条数据
        List<OrderDetail> detailList = new ArrayList<>();
        for(ShoppingCart cart:cartRecords){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());

            detailList.add(orderDetail);
        }

        detailMapper.insertBatch(detailList);

        //清空用户的购物车数据
        shoppingCartMapper.clear(userId);

        //封装VO返回结果
        OrderSubmitVO submitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();

        return submitVO;
    }

    /**
     * 订单支付
     * @param paymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO paymentDTO) throws Exception {
        //当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                paymentDTO.getOrderNumber(),    //商户订单号
//                new BigDecimal("0.01"),     //金额
//                "苍穹外卖订单",      //商品描述
//                user.getOpenid()    //微信用户的openid
//        );

        JSONObject jsonObject = new JSONObject();

        if(jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")){
            throw new OrderBusinessException("该订单已支付");
        }
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        return vo;
    }


    /**
     * 支付成功修改订单状态
     * @param outTradeNo
     */
    @Override
    public void paySuccess(String outTradeNo) {
        //当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        //根据订单号查询当前用户的订单
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        //通过websocket向客户端浏览器推送消息
        Map map = new HashMap();

        map.put("type", 1); //1表示来单提醒，2表示客户催单
        map.put("orderId", ordersDB.getId());
        map.put("content","订单号："+outTradeNo);

        String json = JSONObject.toJSONString(map);

        socketServer.sendToAllClient(json);
    }

    /**
     * 历史订单查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult history(Integer page, Integer pageSize, Integer status) {
        Long userId = BaseContext.getCurrentId();

        PageHelper.startPage(page,pageSize);
        Orders orders = Orders.builder()
                .userId(userId)
                .status(status)
                .build();
        Page<OrderVO> records =  orderMapper.page(orders);

        //TODO:解决n+1问题
        for(OrderVO item : records){
            List<OrderDetail> orderDetailList = detailMapper.queryByOrdersId(item.getId());
            item.setOrderDetailList(orderDetailList);
        }

        return new PageResult(records.getTotal(), records.getResult());
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @Override
    public OrderVO queryDetail(Long id) {

        Orders orders = orderMapper.queryById(id);

        OrderVO detail = new OrderVO();
        BeanUtils.copyProperties(orders,detail);

        List<OrderDetail> orderDetailList = detailMapper.queryByOrdersId(id);

        if(orderDetailList != null || !orderDetailList.isEmpty()){
            detail.setOrderDetailList(orderDetailList);
        }

        return detail;
    }

    /**
     * 再来一单
     * @param orderId
     */
    @Override
    public void repetition(Long orderId) {
        OrderVO orderVO = queryDetail(orderId);
        List<OrderDetail> detailList = orderVO.getOrderDetailList();

        Long userId = BaseContext.getCurrentId();

        //先清除购物车
        shoppingCartMapper.clear(userId);

        //从订单中拼到购物车对象
        List<ShoppingCart> cartList = new ArrayList<>();
        for(OrderDetail item: detailList){
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(item,shoppingCart);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(userId);
            cartList.add(shoppingCart);
        }

        shoppingCartMapper.insertBatch(cartList);
    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    public void cancelFromUser(Long id) throws Exception {
        //根据id查询订单
        Orders param = new Orders();
        param.setId(id);
        Orders orders = orderMapper.queryById(id);

        checkAndPopulate(orders,param);

        param.setCancelReason("用户取消");
        orderMapper.update(param);

    }

    /**
     * 订单搜索
     * @param pageQueryDTO
     * @return
     */
    //TODO:参考
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO pageQueryDTO) {
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
        Page<OrderVO> records = orderMapper.search(pageQueryDTO);

        if (records == null || records.getTotal() == 0) {
            return new PageResult(0, new ArrayList<>());
        }

        pushOrderDishes(records);

        return new PageResult(records.getTotal(),records.getResult());
    }

    /**
     * 填充的订单的OrderDishes
     * @param records
     */
    private void pushOrderDishes(List<OrderVO> records){
        List<Long> orderIds = records.stream().map(OrderVO::getId).collect(Collectors.toList());

        List<OrderDetail> orderDetailList = detailMapper.queryByOrdersIds(orderIds);

        Map<Long, List<OrderDetail>> detailMap = orderDetailList.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId));

        for (OrderVO orderVO : records) {
            // 从 Map 中获取当前订单对应的详情列表
            List<OrderDetail> details = detailMap.get(orderVO.getId());

            // 拼接字符串逻辑
            if (details != null && !details.isEmpty()) {
                String dishStr = details.stream()
                        .map(d -> d.getName() + "*" + d.getNumber())
                        .collect(Collectors.joining(";"));
                orderVO.setOrderDishes(dishStr);
            }
        }
    }

    /**
     * 接单
     * @param id
     */
    @Override
    public void confirm(Long id) {
        Orders orders = new Orders();
        orders.setId(id);

        Orders result  = orderMapper.queryById(id);
        if(result == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(result.getStatus() != Orders.TO_BE_CONFIRMED){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setStatus(Orders.CONFIRMED);

        orderMapper.update(orders);
    }

    /**
     * 取消订单异常校验与更新填充
     * @param target
     * @param change
     * @return
     */
    private void checkAndPopulate(Orders target, Orders change){
        if(target == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(!target.getStatus().equals(Orders.TO_BE_CONFIRMED) && !target.getStatus().equals(Orders.CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if(target.getPayStatus().equals(Orders.PAID)){
            //调用微信支付退款接口
//            weChatPayUtil.refund(
//                    result.getNumber(), //商户订单号
//                    result.getNumber(), //商户退款单号
//                    new BigDecimal(0.01),//退款金额，单位 元
//                    new BigDecimal(0.01));//原订单金额

            change.setPayStatus(Orders.REFUND);
        }
        change.setStatus(Orders.CANCELLED);
        change.setCancelTime(LocalDateTime.now());

    }

    /**
     * 商家拒单
     * @param rejectionDTO
     */
    @Transactional
    @Override
    public void rejection(OrdersRejectionDTO rejectionDTO) throws Exception{

        Orders change = new Orders();
        change.setId(rejectionDTO.getId());

        Orders target  = orderMapper.queryById(rejectionDTO.getId());

        checkAndPopulate(target,change);
        change.setRejectionReason(rejectionDTO.getRejectionReason());

        orderMapper.update(change);
    }

    /**
     * 商家取消拒单
     * @param cancelDTO
     */
    @Transactional
    @Override
    public void cancelFromAdmin(OrdersCancelDTO cancelDTO) {
        Orders change = new Orders();
        change.setId(cancelDTO.getId());

        Orders target  = orderMapper.queryById(cancelDTO.getId());

        checkAndPopulate(target, change);


        change.setCancelReason(cancelDTO.getCancelReason());

        orderMapper.update(change);
    }

    /**
     * 派送订单
     * @param id
     */
    @Override
    public void delivery(Long id) {
        Orders change = new Orders();
        change.setId(id);

        Orders target  = orderMapper.queryById(id);

        if(target == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(!target.getStatus().equals(Orders.CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        change.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(change);

    }

    /**
     * 完成订单
     * @param id
     */
    @Override
    public void complete(Long id) {
        Orders change = new Orders();
        change.setId(id);

        Orders target  = orderMapper.queryById(id);

        if(target == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(!target.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        change.setStatus(Orders.COMPLETED);
        change.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(change);
    }

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {

        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);

        OrderStatisticsVO statisticsVO = new OrderStatisticsVO();
        statisticsVO.setConfirmed(confirmed);
        statisticsVO.setDeliveryInProgress(deliveryInProgress);
        statisticsVO.setToBeConfirmed(toBeConfirmed);

        return statisticsVO;
    }

    /**
     * 用户催单
     * @param id
     */
    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.queryById(id);
    }
}