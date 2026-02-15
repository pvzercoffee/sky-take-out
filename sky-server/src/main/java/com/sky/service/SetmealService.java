package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

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

    /**
     * 新增套餐
     * @param setmealDTO 套餐信息
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     * @param id 目标id
     * @param status 要修改的状态
     * @return
     */
    void startOrStop(Long id, Integer status);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO queryById(Long id);

    /**
     * 修改套餐
     * @param setmealDTO 修改内容
     * @return
     */
    void modify(SetmealDTO setmealDTO);

    /**
     * 根据id批量删除套餐
     * @param ids 删除目标
     * @return
     */
    void removeBatch(List<Long> ids);

    /**
     * 根据分类id查询套餐
     * @param setmeal 查询信息
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);
}
