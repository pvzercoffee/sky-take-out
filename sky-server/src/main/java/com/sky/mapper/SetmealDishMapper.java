package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询套餐id
     * @param dishIds 菜品id
     * @return 套餐id
     */
    List<Long> getSetmealIdByDishId(List<Long> dishIds);

    /**
     * 批量添加套餐关联的菜品
     * @param setmealDishes 关联菜品列表
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐与菜品关系
     * @param setmealId
     * @return
     */
    List<SetmealDish> getLinkOfSetmeal(Long setmealId);
}
