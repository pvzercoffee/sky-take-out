package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车管理
 */
@Api(tags = "购物相关接口")
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 从购物车中添加
     * @param cartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("从购物车中添加")
    public Result add(@RequestBody ShoppingCartDTO cartDTO){
        log.info("从购物车中添加:{}",cartDTO);
        shoppingCartService.add(cartDTO);
        return Result.success();
    }
}
