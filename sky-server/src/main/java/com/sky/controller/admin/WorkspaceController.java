package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 工作台相关接口
 */
@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "工作台相关接口")
@Slf4j
public class WorkspaceController{

    @Autowired
    WorkspaceService workspaceService;

    /**
     * 查询今日运营数据
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("查询今日运营数据")
    public Result<BusinessDataVO> businessData(){
        BusinessDataVO result = workspaceService.businessData();
        return Result.success(result);
    }

    /**
     * 查询套餐总览
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("查询套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        SetmealOverViewVO result = workspaceService.overviewSetmeals();
        return Result.success(result);
    }

    /**
     * 查询菜品总览
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("查询菜品总览")
    public Result<DishOverViewVO> overviewDishes(){
        DishOverViewVO result = workspaceService.overviewDishes();
        return Result.success(result);
    }

    /**
     * 查询订单总览
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("查询订单总览")
    public Result<OrderOverViewVO> overviewOrders(){
        OrderOverViewVO result = workspaceService.overviewOrders();
        return Result.success(result);
    }
}
