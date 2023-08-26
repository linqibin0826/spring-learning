package com.linqibin.spring.aware.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author linqibin
 * @Date 2023/8/26 12:31
 * @Email 1214219989@qq.com
 */
@Configuration
@Slf4j
public class MyConfig {


    public MyConfig() {
        log.info("construct");
    }

    @Autowired
    private ApplicationContext application;

    @PostConstruct
    void init() {
        log.info("MyConfig中使用@Autowired方式注入：" + application);
    }

    @Bean
    public BeanFactoryPostProcessor myBeanFactoryPostProcessor() {
        return beanFactory -> {
            log.info("我的beanFactory后置处理器被创建了");
        };
    }
}

