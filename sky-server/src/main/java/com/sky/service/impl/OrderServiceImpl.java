package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        Orders orders = Orders.builder()
                .id(id)
                .userId(BaseContext.getCurrentId())
                .build();

        OrderVO detail = orderMapper.query(orders);
        detail.setOrderDetailList(detailMapper.queryByOrdersId(id));

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
}