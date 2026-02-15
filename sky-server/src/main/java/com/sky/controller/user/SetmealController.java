package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐管理
 */
@Api(tags = "C端-套餐相关接口")
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     * @param categoryId 分类id
     * @return
     */
    @GetMapping
    @ApiOperation("根据分类id查询套餐")
    public Result<List<Setmeal>> list(Long categoryId){
        log.info("根据分类id查询套餐:{}",categoryId);
        Setmeal setmeal = new Setmeal().builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        List<Setmeal> setmealList = setmealService.list(setmeal);
        return Result.success(setmealList);
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id 套餐id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品")
    public Result dishList(@PathVariable  Long id){
         log.info("根据套餐id查询包含的菜品:{}",id);
         List<DishItemVO> items= setmealService.getDishItemById(id);
         return Result.success(items);
    }
}
