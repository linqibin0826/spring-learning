package com.linqibin.spring.aop;


import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * SpringAop选择
 * @Author linqibin
 * @Date 2023/8/28 22:38
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {
        // 准备切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");

        // 准备通知
        MethodInterceptor interceptor = invocation -> {
            System.out.println("before");
            Object proceed = invocation.proceed();
            System.out.println("after");
            return proceed;
        };

        // 准备切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, interceptor);

        // 用Spring的代理工厂生成代理对象
        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvisor(advisor);
        // proxyTargetClass默认是false，设置了interface，则会使用jdk动态代理。
        proxyFactory.setInterfaces(target.getClass().getInterfaces());

        // 如果proxyTargetClass为false，目标对象有接口，并且在proxyFactory中赋值了接口数组，则使用JDK动态代理
        // 如果proxyTargetClass为false，目标对象没有接口，则使用cglib
        // 如果proxyTargetClass为true，则使用的是cglib


        F1 proxy = (F1) proxyFactory.getProxy();
        proxy.foo();
        // class com.linqibin.spring.aop.$Proxy0
        System.out.println(proxy.getClass());

    }

    interface F1 {
        void foo();

        void bar();
    }

    static class Target implements F1 {
        @Override
        public void foo() {
            System.out.println("foo");
        }

        @Override
        public void bar() {
            System.out.println("bar");
        }
    }

    static class TargetNoInterface{
        public void foo() {
            System.out.println("noInterfaceFoo");
        }

        public void bar() {
            System.out.println("noInterfaceBar");
        }
    }

}

