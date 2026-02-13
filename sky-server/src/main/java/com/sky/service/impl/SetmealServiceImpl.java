package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
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

        //新套餐默认为停售状态
        setmeal.setStatus(StatusConstant.DISABLE);

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

    /**
     * 修改套餐
     * @param setmealDTO 修改内容
     * @return
     */
    @Transactional
    @Override
    public void modify(SetmealDTO setmealDTO) {

        //修改套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);

        //删除关联的原菜品
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        //重新设置外键，更保险
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            for(SetmealDish setmealDish : setmealDishes){
                setmealDish.setSetmealId(setmealDTO.getId());
            }

            //插入新的关联菜品
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 根据id批量删除套餐
     * @param ids 删除目标
     * @return
     */
    @Transactional
    @Override
    public void removeBatch(List<Long> ids) {
        // 起售中的套餐不能删除
        List<Setmeal> setmealList = setmealMapper.queryByIds(ids);

        setmealList.forEach(setmeal -> {
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        //TODO:减少查询次数
        ids.forEach(id ->{
            //删除套餐关联菜品
            setmealDishMapper.deleteBySetmealId(id);
            //删除套餐
            setmealMapper.delete(id);

        });
    }
}
