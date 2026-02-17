package com.sky.controller.user;


import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺管理
 */
@Api(tags = "C端-订单相关接口")
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 用户下单
     * @param submitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO submitDTO){

        log.info("用户下单：{}",submitDTO);
        OrderSubmitVO submitVO = orderService.submit(submitDTO);

        return Result.success(submitVO);
    }

    /**
     * 订单支付
     * @param paymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO paymentDTO) throws Exception {
        log.info("订单支付：{}",paymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(paymentDTO);
        log.info("生成预支付交易单:{}",orderPaymentVO);

        //改写一下支付逻辑，改成点击后就可以支付
        orderService.paySuccess(paymentDTO.getOrderNumber());

        return Result.success(orderPaymentVO);
    }

    /**
     * 历史订单查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> history(Integer page, Integer pageSize, Integer status){
        log.info("历史订单查询:第{}页,每页{}条,状态:{}",page,pageSize,status);

        PageResult pageResult = orderService.history(page,pageSize,status);

        return Result.success(pageResult);

    }
}