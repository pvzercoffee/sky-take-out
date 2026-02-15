package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(*) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void save(Dish dish);


    /**
     * 分页分类查询
     * @param name 菜品名称
     * @param categoryId 菜品分类
     * @param status 售卖状态
     */
    Page<DishVO> page(String name, Integer categoryId, Integer status);

    /**
     * 根据id查询菜品
     * @param id 菜品id
     * @return
     */
    @Select("select * from dish where id= #{id}")
    Dish queryById(Long id);

    /**
     * 根据主键删除菜品数据
     * @param id 主键
     */
    @Delete("delete from dish where id = #{id}")
    void delete(Long id);

    /**
     * 根据id批量删除菜品数据
     * @param ids id列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改菜品
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 菜品起售、停售
     * @param id 主键
     * @param status 启停售状态
     */
    @AutoFill(OperationType.UPDATE)
    @Update("update dish set status = #{status} where id = #{id}")
    void startOrStop(Long id, Integer status);

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类主键
     * @return
     */
    List<DishVO> queryByCategoryId(Integer categoryId);
}
