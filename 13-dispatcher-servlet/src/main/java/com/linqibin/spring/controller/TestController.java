package com.linqibin.spring.controller;

import com.linqibin.spring.annotation.Token;
import com.linqibin.spring.annotation.YmlResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @Author linqibin
 * @Date 2023/8/31 22:43
 * @Email 1214219989@qq.com
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    @ResponseBody
    public String test(@RequestParam String hello, @RequestParam String world) throws Exception {
        Method test = TestController.class.getMethod("test", String.class, String.class);
        for (Parameter parameter : test.getParameters()) {
            System.out.println(parameter.getName());
        }

        return "hello get";
    }

    @PostMapping("/post")
    public String test01(@RequestBody String name) {
        System.out.println("My name is " + name);
        return "hello post";
    }

    @RequestMapping("/request")
    public String test02() {
        return "hello request";
    }

    @GetMapping("/token")
    public String tokenTest(@Token String token) {
        return token;
    }

    @GetMapping("/yml")
    @YmlResponse
    public User ymlTest(@Token String token) {
        System.out.println(token);
        return new User("linqibin", "hello");
    }

    @Data
    @AllArgsConstructor
    public static class User {


        private String username;

        private String password;
    }
}

