package com.sky.service.impl;

import com.sky.entity.TurnoverReport;
import com.sky.entity.UserReport;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
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
    OrderMapper orderMapper;
    @Autowired
    private EnumTypeDeterminer enumTypeDeterminer;

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
}
