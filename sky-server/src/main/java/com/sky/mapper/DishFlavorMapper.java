package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味
     * @param flavorList 口味列表
     */
    void insertBatch(List<DishFlavor> flavorList);

    /**
     * 根据菜品主键删除口味
     * @param dishId 菜品主键
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品主键批量删除口味
     * @param dishIds 菜品主键
     */
    void deleteByDishIds(List<Long> dishIds);
}
