package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 历史订单查询
     * @param order
     * @return
     */
    Page<OrderVO> page(Orders order);

    /**
     * 根据id查询订单
     * @param orders
     * @return
     */
    OrderVO query(Orders orders);

    /**
     * 订单搜索
     * @param queryDTO
     * @return
     */
    Page<OrderVO> search(OrdersPageQueryDTO queryDTO);

    @Select("select count(*) from orders where status = #{status}")
    Integer countStatus(Integer status);
}