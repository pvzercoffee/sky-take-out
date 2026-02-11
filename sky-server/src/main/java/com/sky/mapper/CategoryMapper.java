package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     * @param category
     */
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user) value" +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Category category);

    /**
     * 分类分页查询
     * @param name
     * @param type
     * @return
     */
    Page<Category> pageQuery(String name, Integer type);

    /**
     * 根据类型查询分页
     * @param type
     * @return
     */
    @Select("select * from category where type = #{type}")
    List<Category> queryByType(Integer type);

    /**
     * 修改分类
     * @param category
     */
    void update(Category category);

    /**
     * 删除分类
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);
}
