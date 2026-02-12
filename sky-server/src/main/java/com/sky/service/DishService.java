package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {
    /**
     * 添加菜品
     * @param dishDTO
     */
    void saveWidthFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param pageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO pageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void removeBatch(List<Long> ids);
}
