package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO queryByIdWidthFlavor(Long id);

    /**
     * 修改菜品及对应口味
     * @param dishDTO 菜品信息
     */
    void modifyWidthFlavor(DishDTO dishDTO);

    /**
     * 菜品起售、停售
     * @param id 主键
     * @param status 启停售状态
     */
    void startOrStop(Long id,Integer status);
}
