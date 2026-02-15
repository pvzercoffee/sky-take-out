package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺管理
 */
@Api(tags = "C端-店铺相关接口")
@RestController("adminShopController")  //防止Bean名称冲突
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     * @param status 状态
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态为：" + (status == 1 ? "营业中" : "打样中"));

        redisTemplate.opsForValue().set(RedisConstant.SHOP_STATUS,status);
        return Result.success();
    }

    /**
     * 查询店铺营业状态
     * @return 状态
     */
    @GetMapping("/status")
    @ApiOperation("查询店铺营业状态")
    public Result<Integer> getStatus(){

        Integer status = (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);

        log.info("查询到店铺营业状态为：" + (status == 1 ? "营业中" : "打样中"));
        return Result.success(status);
    }
}
