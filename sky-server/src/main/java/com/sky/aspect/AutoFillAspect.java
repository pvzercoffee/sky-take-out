package com.sky.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;import org.aspectj.lang.annotation.Pointcut;import org.springframework.stereotype.Component;

//自定义切面类,实现公共字段子自动处理逻辑

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
//        log.info("开始进行公共字段自动填充...");
    }
}
