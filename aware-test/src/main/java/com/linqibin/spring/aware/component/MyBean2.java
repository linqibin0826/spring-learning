package com.linqibin.spring.aware.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author linqibin
 * @Date 2023/8/26 12:54
 * @Email 1214219989@qq.com
 */
@Component
@Slf4j
public class MyBean2 {

    @Autowired
    private ApplicationContext application;

    @PostConstruct
    void init() {
        log.info("使用@Autowired方式注入：" + application);
    }
}

