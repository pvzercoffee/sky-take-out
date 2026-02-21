package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 查询今日运营数据
     * @return
     */
    BusinessDataVO businessData(LocalDateTime begin,LocalDateTime end);

    /**
     * 查询套餐总览
     * @return
     */
    SetmealOverViewVO overviewSetmeals();

    /**
     * 查询菜品总览
     * @return
     */
    DishOverViewVO overviewDishes();

    /**
     * 查询订单总览
     * @return
     */
    OrderOverViewVO overviewOrders();
}
