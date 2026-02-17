package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 从购物车中添加
     * @param shoppingCart
     */
    void add(ShoppingCart shoppingCart);

    /**
     * 动态查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> query(ShoppingCart shoppingCart);

    /**
     * 通过id更新数量
     * @param id
     * @param number
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(Long id,Integer number);

    /**
     * 动态删除购物车中的数据
     * @param shoppingCart
     */
    @Delete("delete from shopping_cart where user_id = #{userId} and dish_id = #{dishId} and setmeal_id = #{setmealId} and dish_flavor = #{dishFlavor}")
    void delete(ShoppingCart shoppingCart);

    /**
     * 根据id删除购物车中的数据
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

    /**
     * 清空用户的购物车
     */
    @Delete("delete from shopping_cart where  user_id = #{userId}")
    void clear(Long userId);

    /**
     * 从购物车中批量添加
     * @param cartList
     */
    void insertBatch(List<ShoppingCart> cartList);
}
