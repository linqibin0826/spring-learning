package com.linqibin.spring.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author linqibin
 * @Date 2023/9/7 22:27
 * @Email 1214219989@qq.com
 */
@Configuration
public class WebConfig {

    @ControllerAdvice
    public static class ControllerAdvice1 {
        @ModelAttribute("model1")
        public String model1() {
            return "testModel";
        }
    }

    @Controller
    public static class Controller1 {

        @ModelAttribute("model2")
        public String model1() {
            return "controllerModel";
        }

        @ResponseStatus(HttpStatus.OK)
        public ModelAndView test(User user) {
            System.out.println(user);
            return null;
        }
    }

    @Data
    public static class User {
        private String name;
    }
}

