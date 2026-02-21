package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 数据统计
 */
@Api(tags = "数据统计相关接口")
@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("营业额统计:{},{}",begin,end);

        TurnoverReportVO turnoverReportVO = reportService.turnoverReport(begin,end);

        return Result.success(turnoverReportVO);
    }

    /**
     * 用户统计
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("用户统计:{},{}",begin,end);

        UserReportVO userReportVO = reportService.userReport(begin,end);

        return Result.success(userReportVO);
    }

    /**
     * 订单统计
     * @return
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("用户统计")
    public Result<OrderReportVO> orderReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("订单统计:{},{}",begin,end);

        OrderReportVO orderReport = reportService.orderReport(begin,end);

        return Result.success(orderReport);
    }

    /**
     * top10销量统计
     * @return
     */
    @GetMapping("/top10")
    @ApiOperation("top10销量统计")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("top10销量统计:{},{}",begin,end);

        SalesTop10ReportVO reportVO  = reportService.top10(begin,end);

        return Result.success(reportVO);
    }

    /**
     * 导出运营数据
     * @param response
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据")
    public void export(HttpServletResponse response) throws IOException {

        reportService.exportBusniessData(response);
    }
}
