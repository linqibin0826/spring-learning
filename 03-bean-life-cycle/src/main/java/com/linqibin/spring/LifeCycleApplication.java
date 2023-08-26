package com.linqibin.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 测试Bean的生命周期
 *
 * @Author linqibin
 * @Date 2023/8/21 23:12
 * @Email 1214219989@qq.com
 */
@SpringBootApplication
@Slf4j
public class LifeCycleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LifeCycleApplication.class, args);
        context.close();
    }

    @Component("bean01")
    static class Bean01 {

        public Bean01() {
            log.error("Bean实例化，构造函数被执行~~~~~~~~~");
        }

        @PostConstruct
        public void init() {
            log.error("Bean初始化阶段~~~~~~~~~~~~~~~");
        }

        @Autowired
        private void autowired(@Value("${JAVA_HOME}") String javaHome) {
            log.error("依赖注入阶段~~~~~~~~~~~~~~~~~{}", javaHome);
        }

        @PreDestroy
        private void destroy() {
            log.error("bean销毁阶段~~~~~~~~~~~~~~~~");
        }

    }

    @Component
    static class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {


        @Override
        public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
            // SpringBoot中的应用程序比较多，因此我们只看自己定义的那个Bean01
            if (beanName.equals("bean01")) {
                log.error(">>>>>>>>>> BeforeInstantiation: 在实例化之前被执行~");
            }
            return null;
        }

        @Override
        public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
            if (beanName.equals("bean01")) {
                log.error(">>>>>>>>>> AfterInstantiation: 在实例化之后被执行~");
            }
            return true;
        }

        @Override
        public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
            if (beanName.equals("bean01")) {
                log.error(">>>>>>>>>> postProcessProperties: 在依赖注入之前被执行~");
            }
            return pvs;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (beanName.equals("bean01")) {
                log.error(">>>>>>>>>> BeforeInitialization: 在初始化之前被执行~");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (beanName.equals("bean01")) {
                log.error(">>>>>>>>>> AfterInitialization: 在初始化之后被执行~");
            }
            return bean;
        }

        @Override
        public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
            if (beanName.equals("bean01")) {
                log.error(">>>>>>>>>> BeforeDestruction: 比Bean销毁方法先执行~");
            }
        }
    }
}

