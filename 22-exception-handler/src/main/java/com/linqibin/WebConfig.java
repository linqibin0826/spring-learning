package com.linqibin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linqibin
 * @date 2023/9/9 20:35
 * @email 1214219989@qq.com
 */
@Configuration
public class WebConfig {

    @RestControllerAdvice
    public static class Advice1 {

        @ExceptionHandler
        public Map<String, String> handle(Exception e, ServletRequest request) {
            Map<String, String> map = new HashMap<>();
            map.put("err", e.getMessage());
            System.out.println(request);
            return map;
        }
    }

    public static class Controller1 {

        public void test01() {
        }

//        @ExceptionHandler
        @ResponseBody
        public Map<String, String> handle(NullPointerException exception) {
            HashMap<String, String> error = new HashMap<>();
            error.put("error", exception.getMessage());
            return error;
        }
    }
}

