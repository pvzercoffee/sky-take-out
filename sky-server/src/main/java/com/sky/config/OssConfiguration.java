package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类e,用于创建AliOssUtil对象
 */
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean //如果容器中没有AliOssUtil对象,则创建一个AliOssUtil对象
    public AliOssUtil aliOssUtil(AliOssProperties properties){
        log.info("开始创建阿里云文件上传工具类对象:{}",properties);
        //创建AliOssUtil对象,并将AliOssProperties对象中的属性值传递给AliOssUtil对象的构造方法
        return new AliOssUtil(properties.getEndpoint(),  properties.getAccessKeyId(),
                properties.getAccessKeySecret(), properties.getBucketName());
    }
}
