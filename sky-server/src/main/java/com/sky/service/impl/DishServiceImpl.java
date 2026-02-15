package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper flavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 添加菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWidthFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表添加一条数据
        dishMapper.save(dish);


        List<DishFlavor> flavor = dishDTO.getFlavors();
        if(!flavor.isEmpty()){
            flavor.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            //向口味表添加n条数据
            flavorMapper.insertBatch(flavor);
        }
    }

    /**
     * 菜品分页查询
      * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO pageQueryDTO) {

        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());

        Page<DishVO> records =  dishMapper.page(pageQueryDTO.getName(),pageQueryDTO.getCategoryId(),pageQueryDTO.getStatus());

        return new PageResult(records.getTotal(),records.getResult());

    }

    /**
     * 菜品批量删除
     * @param ids
     */
    @Transactional
    @Override
    public void removeBatch(List<Long> ids) {
        //是否存在起售中的菜品
        for(Long id: ids){
            Dish dish = dishMapper.queryById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //菜品是否被套餐关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdByDishId(ids);
        if(!setmealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }


        //删除菜品数据
        dishMapper.deleteByIds(ids);

        //删除关联口味数据
        flavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品及口味
     * @param id
     * @return
     */
    @Override
    public DishVO queryByIdWidthFlavor(Long id) {

        //根据id查询菜品数据
        Dish dish = dishMapper.queryById(id);

        //根据菜品id查询口味数据
        List<DishFlavor> flavorList = flavorMapper.queryByDishId(id);

        //将查询到的口味数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);

        dishVO.setFlavors(flavorList);

        return dishVO;
    }

    /**
     * 修改菜品及对应口味
     * @param dishDTO 菜品信息
     */
    @Transactional
    @Override
    public void modifyWidthFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        flavorMapper.deleteByDishId(dishDTO.getId());

        List<DishFlavor> flavor = dishDTO.getFlavors();
        if(!flavor.isEmpty()){
            flavor.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            //向口味表添加n条数据
            flavorMapper.insertBatch(flavor);
        }

        dishMapper.update(dish);
    }

    /**
     * 菜品起售、停售
     * @param id 主键
     * @param status 启停售状态
     */
    @Override
    public void startOrStop(Long id,Integer status) {
        dishMapper.startOrStop(id,status);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类主键
     * @return
     */
    @Override
    public List<DishVO> list(Integer categoryId) {
        List<DishVO> records =  dishMapper.queryByCategoryId(categoryId);

        for(DishVO dishVO : records){
            List<DishFlavor> flavorList = flavorMapper.queryByDishId(dishVO.getId());
            dishVO.setFlavors(flavorList);
        }

        return records;

    }
}
