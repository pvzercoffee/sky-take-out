package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.entity.OrderReport;
import com.sky.entity.SalesTop10Report;
import com.sky.entity.TurnoverReport;
import com.sky.entity.UserReport;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spi.schema.EnumTypeDeterminer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end) {

        //TODO:解决n+1问题的示例
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        //把LocalDate类转换为LocalDateTime，便于数据库查询

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);

//        拼接日期字符串
        while(!begin.equals(end))
        {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //获取到日期与当日营业额的列表
        List<TurnoverReport> ordersList = orderMapper.turnoverStatistics(beginTime,endTime);

        //把列表转换成<日期,营业额>的map对
        Map<LocalDate, Double> turnoverMap = ordersList.stream()
                .collect(Collectors.toMap(TurnoverReport::getOrderDate, TurnoverReport::getTurnover));

        //把日期列表与结果的金额重新组成金额，不存在的日期getOrDefault把金额默认为0
        List<Double> resultList = dateList.stream()
                .map(date -> turnoverMap.getOrDefault(date, 0.0)) // 这一步就是“补值”的核心
                .collect(Collectors.toList());

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(resultList,","))
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userReport(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();

        //把LocalDate类转换为LocalDateTime，便于数据库查询

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);

//        拼接日期字符串
        LocalDate localDate = begin;
        dateList.add(localDate);

        while(!localDate.equals(end))
        {
            localDate = localDate.plusDays(1);
            dateList.add(localDate);
        }

        List<UserReport> newStatistics = orderMapper.userNewStatistics(beginTime,endTime);

        //把列表转换成<日期,营业额>的map对
        Map<LocalDate, Integer> newUserMap = newStatistics.stream()
                .collect(Collectors.toMap(UserReport::getCreateDate, UserReport::getNum));

        //把日期列表与结果的金额重新组成金额，不存在的日期getOrDefault把金额默认为0
        List<Integer> newList = dateList.stream()
                .map(date -> newUserMap.getOrDefault(date, 0)) // 这一步就是“补值”的核心
                .collect(Collectors.toList());
        

        Integer theDayBeforeTotal = orderMapper.userTotalStatistics(beginTime.minusDays(1));
        
        List<Integer> totalList = new ArrayList<>();

        for(Integer i : newList){
            theDayBeforeTotal += i;
            totalList.add(theDayBeforeTotal);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newList,","))
                .totalUserList(StringUtils.join(totalList,","))
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    //TODO:合并对象简化逻辑
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();

        //把LocalDate类转换为LocalDateTime，便于数据库查询

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);

//        拼接日期字符串
        LocalDate localDate = begin;
        dateList.add(localDate);

        while(!localDate.equals(end))
        {
            localDate = localDate.plusDays(1);
            dateList.add(localDate);
        }

        //获取到日期与当日订单数的列表
        List<OrderReport> ordersList = orderMapper.orderStatistics(beginTime,endTime);

        //把列表转换成<日期,日订单数>的map对
        Map<LocalDate, Integer> totalMap = ordersList.stream()
                .collect(Collectors.toMap(OrderReport::getDate, OrderReport::getOrderCount));

        //把日期列表与结果的订单数重新组成，不存在的日期getOrDefault把订单数默认为0
        List<Integer> countList = dateList.stream()
                .map(date -> totalMap.getOrDefault(date, 0)) // 这一步就是“补值”的核心
                .collect(Collectors.toList());

        //把列表转换成<日期,日有效订单数>的map对
        Map<LocalDate, Integer> valideMap = ordersList.stream()
                .collect(Collectors.toMap(OrderReport::getDate, OrderReport::getValidOrderCount));

        //把日期列表与结果的订单数重新组成，不存在的日期getOrDefault把订单数默认为0
        List<Integer> validList = dateList.stream()
                .map(date -> valideMap.getOrDefault(date, 0)) // 这一步就是“补值”的核心
                .collect(Collectors.toList());

        //计算订单总数
        Integer totalOrderCount =  countList.stream().mapToInt(Integer::intValue).sum();

        //计算有效订单总数
        Integer validOrderCount =  validList.stream().mapToInt(Integer::intValue).sum();

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(countList,","))
                .validOrderCountList(StringUtils.join(validList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(totalOrderCount == 0 ? 0.0 : (double)validOrderCount/(double)totalOrderCount)
                .build();
    }

    /**
     * top10销量统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {

        //把LocalDate类转换为LocalDateTime，便于数据库查询

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);

        //获取到菜品或套餐名称与其对应销量的列表
        PageHelper.startPage(1,10);
        Page<SalesTop10Report> top10List = orderDetailMapper.top10(beginTime,endTime);

        //把列表转换成<日期,日订单数>的map对
        List<String> nameList = top10List.stream().map(SalesTop10Report::getName).collect(Collectors.toList());
        List<Integer> numberList = top10List.stream().map(SalesTop10Report::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }
}
