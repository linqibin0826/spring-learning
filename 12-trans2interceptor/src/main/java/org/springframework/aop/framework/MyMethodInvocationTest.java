package org.springframework.aop.framework;

import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 模拟切面调用链执行(递归)
 * @Author linqibin
 * @Date 2023/8/30 21:44
 * @Email 1214219989@qq.com
 */
public class MyMethodInvocationTest implements MethodInvocation {

    public static void main(String[] args) throws Throwable {
        // 最后，模拟一下MethodInvocation中proceed方法的实现。
        Target1 target = new Target1();
        List<MethodInterceptor> interceptors = Lists.newArrayList(new Advice1(), new Advice2());

        MyMethodInvocationTest foo = new MyMethodInvocationTest(target, Target1.class.getMethod("foo"), interceptors);
        foo.proceed();
    }

    static class Target1{
        public void foo() {
            System.out.println("foo~~~");
        }
    }

    static class Advice1 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {

            System.out.println("before1");
            Object proceed = invocation.proceed();
            System.out.println("after1");
            return proceed;
        }
    }

    static class Advice2 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {

            System.out.println("before2");
            Object proceed = invocation.proceed();
            System.out.println("after2");
            return proceed;
        }
    }

    private Object target;

    private Method method;

    private List<MethodInterceptor> methodInterceptors;

    private Integer count = 0;

    public MyMethodInvocationTest(Object target, Method method, List<MethodInterceptor> methodInterceptors) {
        this.target = target;
        this.method = method;

        this.methodInterceptors = methodInterceptors;
    }



    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Object proceed() throws Throwable {
        // 递归调用
        Object result;
        if (count > methodInterceptors.size() - 1) {
            result = method.invoke(target);
            return result;
        }

        return (methodInterceptors.get(count++)).invoke(this);
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }

}

