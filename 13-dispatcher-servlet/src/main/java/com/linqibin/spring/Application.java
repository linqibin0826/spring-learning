package com.linqibin.spring;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

/**
 * DispatcherServlet初始化时机
 *
 * @Author linqibin
 * @Date 2023/8/30 22:45
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);


    }



}

