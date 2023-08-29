package org.springframework.aop.framework.autoproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

/**
 * 测试Spring Aop的两个关键方法
 *
 *
 * @Author linqibin
 * @Date 2023/8/29 20:40
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {

        GenericApplicationContext context = new GenericApplicationContext();
        // 低级切面配置类
        context.registerBean(Config.class);
        // 高级切面
        context.registerBean(Aspect1.class);
        context.registerBean(ConfigurationClassPostProcessor.class);


        // 刷新容器
        context.refresh();

        // Spring Aop的核心类，包括找到有【资格】的【advisor】、将高级切面转换成低级切面、是否有必要创建代理对象
        // ①：findEligibleAdvisors()    ②：wrapIfNecessary()
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);

        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        // 如果有切面的话， Spring会默认加上一个  org.springframework.aop.interceptor.ExposeInvocationInterceptor.ADVISOR
        List<Advisor> advisors = creator.findEligibleAdvisors(Target1.class, "target1");

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");

        advisors.forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // wrapIfNecessary() 回去调用上面那个方法， 如果有切面， 则会创建代理对象。
        Target1 wrapped = (Target1) creator.wrapIfNecessary(new Target1(), "target1", "target1");
        // Application$Target1$$EnhancerBySpringCGLIB$$b2588110
        System.out.println(wrapped.getClass());
        /*
            advisor before~
            Aspect Before
            foo~~~
            Aspect After
            advisor after~
         */
        wrapped.foo();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // 不会被代理
        Target2 target2 = (Target2) creator.wrapIfNecessary(new Target2(), "Target2", "Target2");
        // bar~~~~~~
        target2.bar();

        // 代理的创建时机 1.对象初始化之后（正常的Bean，无循环依赖时） 2.实例化后，依赖注入前（有循环依赖时）  并暂存在二级缓存
        // 注入与初始化不应该被增强，仍应被施加于原始对象。

    }



    static class Target1{
        public void foo() {
            System.out.println("foo~~~");
        }
    }

    static class Target2{
        public void bar() {
            System.out.println("bar~~~~~~");
        }
    }

    @Aspect
    static class Aspect1{

        @Before("execution(* foo())")
        public void before() {
            System.out.println("Aspect Before");
        }

        @After("execution(* foo())")
        public void after() {
            System.out.println("Aspect After");
        }
    }

    @Configuration
    static class Config{

        @Bean
        public DefaultPointcutAdvisor advisor() {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");

            MethodInterceptor interceptor = new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("advisor before~");
                    Object proceed = invocation.proceed();
                    System.out.println("advisor after~");
                    return proceed;
                }
            };

            return new DefaultPointcutAdvisor(pointcut, interceptor);
        }
    }
}

