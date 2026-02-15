package com.sky.mapper;

import com.sky.entity.ShoppingCart;
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
}
