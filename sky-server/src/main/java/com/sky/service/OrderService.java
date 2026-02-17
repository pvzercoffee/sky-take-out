package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

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
}
