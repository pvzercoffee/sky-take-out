package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.vo.OrdersVO;

import java.util.List;

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
}
