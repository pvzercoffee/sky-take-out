package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     * @param submitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO submitDTO);

    /**
     * 订单支付
     * @param paymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO paymentDTO) throws Exception;

    /**
     * 支付成功修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult history(Integer page, Integer pageSize, Integer status);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO queryDetail(Long id);

    /**
     * 再来一单
     * @param orderId
     */
    void repetition(Long orderId);

    /**
     * 取消订单
     * @param id
     */
    void cancel(Long id) throws Exception;


    /** 订单搜索
     * pageQueryDTO
     * @param pageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO pageQueryDTO);
}
