package com.linqibin.spring.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @Author linqibin
 * @Date 2023/8/31 22:43
 * @Email 1214219989@qq.com
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public String test() {
        System.out.println("hello get");
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
}

