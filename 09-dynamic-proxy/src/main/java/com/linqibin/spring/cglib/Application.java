package com.linqibin.spring.cglib;

import com.linqibin.spring.Target;
import com.linqibin.spring.TargetImpl;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * 使用cglib的动态代理
 *
 * @Author linqibin
 * @Date 2023/8/27 21:04
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {

        TargetImpl target = new TargetImpl();
        Target proxyObj = (Target) Enhancer.create(TargetImpl.class, (MethodInterceptor) (obj, method, args1, proxy) -> {
            System.out.println("before");
            // 内部使用反射调用目标，效率低
//            method.invoke(target, args1);
            // 内部没有使用反射
//            proxy.invoke(target, args1);
            proxy.invokeSuper(obj, args);
            return null;
        });

        proxyObj.show();
    }
}

