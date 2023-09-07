package com.linqibin.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * @Author linqibin
 * @Date 2023/9/7 21:00
 * @Email 1214219989@qq.com
 */
@Configuration
public class WebConfig {

    WebConfig() {

    }

    @ControllerAdvice
    static class Advice {

        @InitBinder
        public void initBinder1(WebDataBinder binder) {

        }

    }
    @Controller
    public static class Controller1 {

        @InitBinder
        public void initBinder2(WebDataBinder binder) {

        }

        @InitBinder
        public void initBinder3(WebDataBinder binder) {

        }

        @GetMapping("/test")
        public String test() {
            return "test";
        }
    }
}

