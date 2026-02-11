package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     *
     * @param name
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    PageResult page(String name, Integer page, Integer pageSize, Integer type);

}
