package com.linqibin.spring.jdk;

import com.linqibin.spring.Target;
import com.linqibin.spring.TargetImpl;

import java.lang.reflect.Proxy;

/**
 * @Author linqibin
 * @Date 2023/8/27 19:53
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {

        TargetImpl target = new TargetImpl();

        Target proxyInstance = (Target)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Target.class},
                (proxy, method, args1) -> {
            System.out.println("before");
            Object invoke = method.invoke(target, args1);
            return invoke;
        });
        System.out.println(proxyInstance.getClass());
        proxyInstance.show();

    }
}

