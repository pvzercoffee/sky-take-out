package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 套餐管理
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO 套餐信息
     */
    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        // 新建套餐
        setmealMapper.insert(setmeal);

        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        if(setmealDishList == null || setmealDishList.isEmpty()) return;

        for(SetmealDish setmealDish: setmealDishList){
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.insertBatch(setmealDishList);

    }

    /**
     * 套餐分页查询
     * @param queryDTO 查询依据
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO queryDTO) {

        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<SetmealVO> records = setmealMapper.page(queryDTO.getName(), queryDTO.getCategoryId(), queryDTO.getStatus());
        PageResult  pageResult = new PageResult(records.getTotal(),records.getResult());

        return pageResult;
    }

    /**
     * 套餐起售、停售
     * @param id 目标id
     * @param status 要修改的状态
     */
    @Override
    public void startOrStop(Long id, Integer status) {
        setmealMapper.startOrStop(id,status);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO queryById(Long id) {

        SetmealVO setmealVO = new SetmealVO();

        Setmeal setmeal = setmealMapper.queryById(id);
        BeanUtils.copyProperties(setmeal,setmealVO);

        List<SetmealDish> setmealDishs =setmealDishMapper.getLinkOfSetmeal(id);
        setmealVO.setSetmealDishes(setmealDishs);

        return setmealVO;
    }
}
