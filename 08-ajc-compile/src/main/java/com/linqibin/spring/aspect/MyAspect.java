package com.linqibin.spring.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Author linqibin
 * @Date 2023/8/26 16:10
 * @Email 1214219989@qq.com
 */
@Aspect
public class MyAspect {


    @Pointcut("execution(* com.linqibin.spring.component.MyComponent.*(..))")
    private void myPointcut() {

    }

    @Before("myPointcut()")
    public void enhance() {
        System.out.println("被编译器增强了， 不使用代理的增强~~~~");
    }
}

