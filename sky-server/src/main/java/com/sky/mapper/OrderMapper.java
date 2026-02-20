package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.*;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 各个状态的订单数量统计
     * @param status
     * @return
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 查询指定时间之前的某状态订单
     * @param status
     * @param time
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndTimeLT(Integer status, LocalDateTime time);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders queryById(Long id);

    /**
     * 营业额统计
     * @return
     */
    List<TurnoverReport> turnoverStatistics(LocalDateTime begin, LocalDateTime end);

    /**
     * 新增用户统计
     * @param begin
     * @param end
     * @return
     */
    List<UserReport> userNewStatistics(LocalDateTime begin, LocalDateTime end);


    /**
     * 累计用户统计
     * @param localDateTime
     * @return
     */
    @Select("select count(*) from user where create_time < #{localDateTime}")
    Integer userTotalStatistics(LocalDateTime localDateTime);


    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    List<OrderReport> orderStatistics(LocalDateTime begin, LocalDateTime end);


    /**
     * 查询订单管理数据
     * @return
     */
    @Select("select " +
            "count(status) allOrders," +
            "sum(status=6) cancelledOrders," +
            "sum(status=5) completedOrders," +
            "sum(status=3) deliveredOrders," +
            "sum(status=2) waitingOrders" +
            " from orders")
    OrderOverViewVO overview();
}