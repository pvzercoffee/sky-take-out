package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 从购物车中添加
     * @param cartDTO
     */
    void add(ShoppingCartDTO cartDTO);

    /**
     * 从购物车中删除一条
     * @param cartDTO
     */
    void delete(ShoppingCartDTO cartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> query();

    /**
     * 清空购物车
     */
    void clear();
}
