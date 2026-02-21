package com.sky.service.impl;

import com.sky.entity.OrderReport;
import com.sky.entity.TurnoverReport;
import com.sky.entity.UserReport;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 查询运营数据
     * @return
     */
    @Override
    public BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end) {

        List<TurnoverReport> reportList = orderMapper.turnoverStatistics(begin,end);

        //查询营业额
        Double turnover = 0.0;
        if(reportList != null && !reportList.isEmpty()){
            turnover = reportList.get(0).getTurnover();
        }

        //查询有效订单数、订单完成率、平均客单价
        Integer validOrderCount = 0;
        Double orderCompletionRate = 0.0;
        Double unitPrice = 0.0;

        List<OrderReport> orderReportList =  orderMapper.orderStatistics(begin,end);
        if(orderReportList != null && !orderReportList.isEmpty()){
            validOrderCount = orderReportList.get(0).getValidOrderCount();
            int totalOrderCount = orderReportList.get(0).getOrderCount();
            orderCompletionRate = totalOrderCount == 0 ? 0.0 : (double)validOrderCount/totalOrderCount;
            unitPrice = validOrderCount == 0 ? 0.0 : turnover/validOrderCount;
        }

        //查询有效用户数
        Integer newUsers = 0;
        List<UserReport> userReportList = orderMapper.userNewStatistics(begin,end);
        if(userReportList != null && !userReportList.isEmpty()){
            newUsers = userReportList.get(0).getNum();
        }

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    /**
     * 查询套餐总览
     * @return
     */
    @Override
    public SetmealOverViewVO overviewSetmeals() {
        return setmealMapper.overview();
    }

    /**
     * 查询菜品总览
     * @return
     */
    @Override
    public DishOverViewVO overviewDishes(){
        return dishMapper.overview();
    }

    /**
     * 查询订单总览
     * @return
     */
    @Override
    public OrderOverViewVO overviewOrders() {
        return orderMapper.overview();
    }
}
