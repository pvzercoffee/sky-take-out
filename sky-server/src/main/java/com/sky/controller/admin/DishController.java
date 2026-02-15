package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@Api(tags = "菜品相关接口")
@RestController("adminDishController")
@Slf4j
@RequestMapping("admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("添加菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWidthFlavor(dishDTO);

        //清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO pageQueryDTO){
        log.info("菜品分页查询：{}",pageQueryDTO);
        PageResult pageResult = dishService.page(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result remove(@RequestParam List<Long> ids){ //必须要加@RequestParam
        log.info("菜品批量删除:{}",ids);
        dishService.removeBatch(ids);

        //清理所有菜品缓存
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> queryById(@PathVariable Long id){
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO = dishService.queryByIdWidthFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @param dishDTO 菜品信息
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result modify(@RequestBody  DishDTO dishDTO){
        log.info("修改菜品:{}",dishDTO);
        dishService.modifyWidthFlavor(dishDTO);

        //清理所有菜品缓存
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 菜品起售、停售
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售、停售")
    public Result startOrStop(@RequestParam Long id,@PathVariable Integer status){
        dishService.startOrStop(id,status);

        //清理所有菜品缓存
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类主键
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId){
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        List<DishVO> records = dishService.list(dish);
        return Result.success(records);
    }

    /**
     * 清理缓存数据
     * @param pattern key模式
     */
    private void cleanCache(String pattern){
        //将所有的菜品缓存数据清除
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
