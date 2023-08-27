package com.linqibin.spring;

import com.linqibin.spring.component.MyComponent;

/**
 * 测试aspectJ的使用编译器aop实现
 * @Author linqibin
 * @Date 2023/8/26 16:09
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {

        // 要实现这种aop， 需要在pom使用aspectJ的编译器
        MyComponent.sayHello();
    }
}

