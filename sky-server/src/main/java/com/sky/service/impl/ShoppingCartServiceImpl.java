package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 从购物车中添加
     * @param cartDTO
     */
    @Override
    public void add(ShoppingCartDTO cartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(cartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //若已存在相同的购物项则直接更新数量并返回
        List<ShoppingCart> result = shoppingCartMapper.query(shoppingCart);
        if(result != null && !result.isEmpty()){
            ShoppingCart resultData = result.get(0);
            shoppingCartMapper.updateNumberById(resultData.getId(),resultData.getNumber() + 1);
            return;
        }

        Long dishId = shoppingCart.getDishId();
        //若dishId非空则获取dish
        if(dishId != null){
            Dish dish = dishMapper.queryById(dishId);
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
        }
        //否则获取setmeal
        else{
            Setmeal setmeal = setmealMapper.queryById(shoppingCart.getSetmealId());
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());
        }

        //补齐字段
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());

        shoppingCartMapper.add(shoppingCart);

    }

    /**
     * 从购物车中删除一条
     * @param cartDTO
     */
    @Override
    public void delete(ShoppingCartDTO cartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(cartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> records = shoppingCartMapper.query(shoppingCart);

        if(records != null && !records.isEmpty()){
            ShoppingCart item = records.get(0);
            if(item.getNumber() > 1){
                shoppingCartMapper.updateNumberById(item.getId(),item.getNumber() - 1);
                return;
            }
            shoppingCartMapper.deleteById(item.getId());
        }


    }

    @Override
    public List<ShoppingCart> query() {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        return shoppingCartMapper.query(shoppingCart);
    }

    @Override
    public void clear() {

    }
}
