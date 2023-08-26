package com.linqibin.spring.impl;

import lombok.Data;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

import java.util.Arrays;

/**
 * 测试ApplicationContext实现类
 *
 * @Author linqibin
 * @Date 2023/8/20 18:51
 * @Email 1214219989@qq.com
 */
public class AcImplApplication {

    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();
//        testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();
    }

    /**
     * 1. 基于配置文件的ApplicationContext实现类
     */
    public static void testClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        // 获取注入的bean
        Bean02 bean02 = (Bean02) context.getBean("bean02");
        System.out.println(bean02.getBean01());
    }

    /**
     * 2. 基于注解文件的ApplicationContext实现类
     */
    public static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        // 获取注入的bean
        Bean02 bean02 = (Bean02) context.getBean("bean02");
        System.out.println(bean02.getBean01());
    }

    /**
     * 3. 基于注解和ServletWebServer应用容器支持的ApplicationContext实现类
     */
    public static void testAnnotationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }

    @Configuration
    static class WebConfig {

        /**
         * 启动内嵌的Tomcat
         * @return
         */
        @Bean
        public TomcatServletWebServerFactory tomcatServletWeb() {
            return new TomcatServletWebServerFactory();
        }

        /**
         * 需要有前端调度器
         * @return
         */
        @Bean
        public DispatcherServlet dispatcherServlet() {
            return new DispatcherServlet();
        }

        /**
         * 将前面两者关联起来
         * @param dispatcherServlet
         * @return
         */
        @Bean
        public RegistrationBean dispatcherRegistrationBean(DispatcherServlet dispatcherServlet) {
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        @Bean("/hello")
        public Controller helloController() {
            return (request, response) -> {
                response.getWriter().write("hello");
                return null;
            };
        }
    }

    @Configuration
    static class Config {

        @Bean
        public Bean01 bean01() {
            return new Bean01();
        }

        @Bean
        public Bean02 bean02(Bean01 bean01) {
            Bean02 bean02 = new Bean02();
            bean02.setBean01(bean01);
            return bean02;
        }
    }


    static class Bean01{

        public Bean01() {
            System.out.println("Bean01构造函数~~~~");
        }
    }

    @Data
    static class Bean02{

        private Bean01 bean01;

        public Bean02() {
            System.out.println("Bean02构造函数");
        }
    }

}

