package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderDetailMapper detailMapper;

    /**
     * 处理超时订单的方法
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){

        log.info("定时处理超时订单：{}", LocalDateTime.now());

        LocalDateTime targetTime = LocalDateTime.now().minusMinutes(15);

        List<Orders> ordersList = orderMapper.getByStatusAndTimeLT(Orders.PENDING_PAYMENT,targetTime);

        //TODO:N+1问题
        if(ordersList != null && !ordersList.isEmpty()){
            for(Orders item : ordersList){
                item.setStatus(Orders.CANCELLED);
                item.setCancelReason("支付超时");
                item.setCancelTime(LocalDateTime.now());
                orderMapper.update(item);
            }
        }
    }

    /**
     * 处理一直处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理一直处于派送中的订单：{}", LocalDateTime.now());

        LocalDateTime targetTime = LocalDateTime.now().minusHours(1);
        List<Orders> ordersList = orderMapper.getByStatusAndTimeLT(Orders.DELIVERY_IN_PROGRESS,targetTime);

        //TODO:N+1问题
        if(ordersList != null && !ordersList.isEmpty()){
            for(Orders item : ordersList){
                item.setStatus(Orders.COMPLETED);
                item.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(item);
            }
        }
    }
}
