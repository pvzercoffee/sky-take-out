package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

/**
 * 套餐管理
 */
public interface SetmealService {
    /**
     * 套餐分页查询
     * @param queryDTO 查询依据
     * @return
     */
    PageResult page(SetmealPageQueryDTO queryDTO);
}
