package com.linqibin.spring;

import com.google.common.collect.Lists;
import com.linqibin.spring.component.Component01;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * BeanFactory和ApplicationContext接口
 * @Author linqibin
 * @Date 2023/8/20 1:32
 * @Email 1214219989@qq.com
 */
@SpringBootApplication
public class BeanApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(BeanApplication.class, args);

        /*
            1. 什么是BeanFactory?
                - BeanFactory是ApplicationContext的父接口
                - 真正的Spring核心容器,主要的ApplicationContext实现都【组合】了他的功能。（getBean方法就是在BeanFactory里面定义的）
         */
        System.out.println(context);

        /*
            2. BeanFactory能做什么？
                - 表面上只有getBean。
                - 实际上控制反转、基本的依赖注入、直至Bean的生命周期的各种功能，都有他的实现类提供(e.g. DefaultListableBeanFactory.class)
         */

        // DefaultListableBeanFactory不仅仅实现了BeanFactory，还额外拓展了非常多的接口，例如SingletonBeanRegistry.class（单例），
        // 我们可以试着获取到容器中所有的单例对象（需要通过反射来暴力破解）
        try {
            Field field = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
            field.setAccessible(true);
            // 获取到BeanFactory
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            Map<String, Object> singletons = (Map<String, Object>)field.get(beanFactory);

            // 我在容器里面放了两个自己定义的组件，因此对其过滤了一下。只打印这两个。
            singletons.entrySet().stream().filter(entry -> entry.getKey().startsWith("component"))
                    .forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 测试MessageSource接口的功能
        System.out.println(context.getMessage("hi", null, Locale.CHINA));
        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));

        // 测试ResourcePatternResolver的功能
        Resource[] resources = context.getResources("classpath*:/META-INF/spring.factories");
        Lists.newArrayList(resources).forEach(System.out::println);

        // 测试EnvironmentCapable的功能
        ConfigurableEnvironment environment = context.getEnvironment();
        System.out.println("JAVA_HOME: " + environment.getProperty("java_home"));
        System.out.println("Server port is: " + environment.getProperty("server.port"));

        // 测试ApplicationEventPublisher的功能
        Component01 bean = (Component01) context.getBean("component01");
        bean.handleUserRegistered();
    }
}

