package com.linqibin.spring.aware.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author linqibin
 * @Date 2023/8/26 12:31
 * @Email 1214219989@qq.com
 */
@Component
@Slf4j
public class MyBean1 implements BeanNameAware, ApplicationContextAware, InitializingBean {


    @Override
    public void setBeanName(String name) {
        log.info(name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("初始化");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info(applicationContext.toString());
    }
}

