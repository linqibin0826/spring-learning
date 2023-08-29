package org.springframework.aop.framework.autoproxy;

import com.google.common.collect.Lists;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author linqibin
 * @Date 2023/8/29 22:36
 * @Email 1214219989@qq.com
 */
public class Aspect2AdvisorTest {

    public static void main(String[] args) {
        // 通知对象如何创建
        SingletonAspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect1());

        List<Advisor> advisors = Lists.newArrayList();
        for (Method method : Aspect1.class.getMethods()) {
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
        }

        advisors.forEach(System.out::println);
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
            System.out.println("Aspect Before");
        }

        @Before("execution(* foo())")
        public void before1() {
            System.out.println("Aspect Before1");
        }
    }
}

