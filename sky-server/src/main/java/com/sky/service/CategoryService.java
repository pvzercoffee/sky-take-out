package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     * @param pageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO pageQueryDTO);

    /**
     * 修改分类
     * @param categoryDTO
     */
    void modifyById(CategoryDTO categoryDTO);

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     */
    void startOrStop(Integer status,Long id);
}
