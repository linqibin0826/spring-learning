package com.linqibin;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

/**
 * @author linqibin
 * @date 2023/9/10 12:38
 * @email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {
        new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }
}

