package com.sky.controller.admin;


import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理
 */
@Api(tags = "订单相关接口")
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 订单搜索
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> page(OrdersPageQueryDTO pageQueryDTO){

        log.info("订单搜索：{}",pageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(pageQueryDTO);

        return Result.success(pageResult);

    }

    /**
     * 查看订单详情
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查看订单详情")
    public Result<OrderVO> details(@PathVariable Long id){
        log.info("查看订单详情:{}",id);
        OrderVO orderVO =  orderService.queryDetail(id);

        return Result.success(orderVO);
    }
}
