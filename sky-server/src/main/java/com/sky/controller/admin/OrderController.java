package com.sky.controller.admin;


import com.github.pagehelper.Page;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
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

    /**
     * 商家接单
     * @param ordersDTO
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersDTO ordersDTO){
        log.info("接单：{}",ordersDTO);

        orderService.confirm(ordersDTO.getId());
        return Result.success();
    }

    /**
     * 商家拒单
     * @param rejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("商家拒单")
    public Result reiection(@RequestBody OrdersRejectionDTO rejectionDTO) throws Exception {
        log.info("商家拒单：{}",rejectionDTO);

        orderService.rejection(rejectionDTO);
        return Result.success();
    }

    /**
     * 商家取消订单
     * @param cancelDTO
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("商家取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO cancelDTO) throws Exception {
        log.info("商家取消订单：{}",cancelDTO);

        orderService.cancelFromAdmin(cancelDTO);
        return Result.success();
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id){
        log.info("派送订单：{}",id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id){
        log.info("完成订单：{}",id);
        orderService.complete(id);
        return Result.success();
    }
}
