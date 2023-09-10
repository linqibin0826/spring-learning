package com.linqibin;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

/**
 * @author linqibin
 * @date 2023/9/9 23:11
 * @email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        // 1. 如果没有加任何处理错误的Bean的话，当抛到Tomcat时会由Tomcat处理


    }
}

