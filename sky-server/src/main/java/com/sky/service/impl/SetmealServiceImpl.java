package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 套餐管理
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

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
}
