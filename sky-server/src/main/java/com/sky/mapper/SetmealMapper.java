package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id查询套餐数量
     * @param categoryId
     * @return
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 套餐分页查询
     * @param name 套餐模糊查询名称
     * @param categoryId 分类id
     * @param status 启停售状态
     * @return
     */
    Page<SetmealVO> page(String name, Integer categoryId, Integer status);

    /**
     * 新增套餐
     * @param setmeal 套餐信息
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 套餐起售、停售
     * @param id 目标id
     * @param status 要修改的状态
     * @return
     */
    @Update("update setmeal set status = #{status} where id = #{id}")
    @AutoFill(OperationType.UPDATE)
    void startOrStop(Long id, Integer status);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal queryById(Long id);

    void update(Setmeal setmeal);
}
