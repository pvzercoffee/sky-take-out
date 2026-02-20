package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.OrderDetail;
import com.sky.entity.SalesTop10Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
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

    /**
     * top10销量统计
     * @param begin
     * @param end
     * @return
     */
    Page<SalesTop10Report> top10(LocalDateTime begin, LocalDateTime end);
}
