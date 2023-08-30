package org.springframework.aop.framework;

import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 无论ProxyFactory基于哪种方式创建代理，最后干活（调用advice通知）的是一个MethodInvocation对象
 * a.因为advisor有多个，且一个套用一个，因此需要一个调用链对象， 即MethodInvocation
 * b.MethodInvocation要知道通知有哪些，目标是什么，调用顺序等。
 * c.环绕通知才适合作为通知，因此，before、afterReturning都会被转换成MethodInterceptor后，调用链统一调用（通过适配器模式转换）
 * @Author linqibin
 * @Date 2023/8/29 22:36
 * @Email 1214219989@qq.com
 */
public class GenerateInterceptor {

    public static void main(String[] args) throws Throwable {
        // 通知对象如何创建
        SingletonAspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect1());

        List<Advisor> advisors = Lists.newArrayList();
        for (Method method : Aspect1.class.getMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                Before annotation = method.getAnnotation(Before.class);
                if (annotation != null) {
                    String value = annotation.value();
                    // 切点
                    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                    pointcut.setExpression(value);
                    // 通知   提供切面的工厂，这里用一个单一切面
                    AspectJMethodBeforeAdvice advice = new AspectJMethodBeforeAdvice(method, pointcut, factory);
//                AspectJAfterAdvice;
//                AspectJAroundAdvice;等
                    DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                    advisors.add(advisor);
                }
            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                AfterReturning annotation = method.getAnnotation(AfterReturning.class);
                if (annotation != null) {
                    String value = annotation.value();
                    // 切点
                    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                    pointcut.setExpression(value);
                    // 通知   提供切面的工厂，这里用一个单一切面
                    AspectJAfterReturningAdvice advice = new AspectJAfterReturningAdvice(method, pointcut, factory);
                    DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                    advisors.add(advisor);
                }
            }


        }

        advisors.forEach(System.out::println);


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // 通知统一转换成MethodInterceptor
        ProxyFactory proxyFactory = new ProxyFactory();
        Target1 target = new Target1();
        proxyFactory.setTarget(target);
        // 添加低级切面
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE);
        proxyFactory.addAdvisors(advisors);
        List<Object> interceptors = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target1.class.getMethod("foo"), Target1.class);
        interceptors.forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // 创建调用链对象，去执行
        // 环绕通知s + 目标对象
        MethodInvocation invocation = new ReflectiveMethodInvocation(null, target, Target1.class.getMethod("foo"), new Object[0],
                Target1.class, interceptors
        );
        // 需要把MethodInvocation暴露出去（ThreadLocal），某些通知可能需要这个对象。
        invocation.proceed();


    }





    static class Target1{
        public void foo() {
            System.out.println("foo~~~");
        }
    }

    @Aspect
    static class Aspect1{

        @Before("execution(* foo())")
        public void before() {
            System.out.println("advice Before");
        }

        @AfterReturning("execution(* foo())")
        public void before1() {
            System.out.println("advice AfterReturning");
        }
    }
}

