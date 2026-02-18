package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单明细
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单id查询订单明细
     * @param orderId
     * @return
     */
    @Select("select * from  order_detail where order_id = #{orderId}")
    List<OrderDetail> queryByOrdersId(Long orderId);

    /**
     * 根据id批量查询订单明细
     */
    List<OrderDetail> queryByOrdersIds(List<Long> orderIds);
}
