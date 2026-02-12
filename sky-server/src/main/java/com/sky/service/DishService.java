package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

public interface DishService {
    /**
     * 添加菜品
     * @param dishDTO
     */
    void saveWidthFlavor(DishDTO dishDTO);

    /**
     * 分页分类查询
     * @param pageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO pageQueryDTO);
}
