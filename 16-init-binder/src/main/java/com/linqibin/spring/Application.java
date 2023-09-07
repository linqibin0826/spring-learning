package com.linqibin.spring;

import com.linqibin.spring.config.WebConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 测试@InitBinder
 * Spring的一个扩展点。通过ControllerAdvice添加全局的@InitBinder方法，提供全局自定义类型转换
 * @Author linqibin
 * @Date 2023/9/7 20:59
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) throws Exception{

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(WebConfig.class);

        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        // 初始化时，会加载ControllerAdvice中的initBinder
        handlerAdapter.setApplicationContext(context);
        handlerAdapter.afterPropertiesSet();

        System.out.println(">>>>>>>>>>>>>>>>刚开始>>>>>>>>>>>>>>>>>>>>>");
        showBindMethods(handlerAdapter);
        System.out.println(">>>>>>>>>>>>>>>>刚开始>>>>>>>>>>>>>>>>>>>>>");

        // 当Controller中方法第一次被调用时， 会去初始化getDataBinderFactory时加载该Controller中被标注@InitBinder的方法。 后面就会有缓存加速
        Method getDataBinderFactory = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDataBinderFactory", HandlerMethod.class);
        HandlerMethod handlerMethod = new HandlerMethod(new WebConfig.Controller1(), WebConfig.Controller1.class.getMethod("test"));
        getDataBinderFactory.setAccessible(true);
        getDataBinderFactory.invoke(handlerAdapter, handlerMethod);

        System.out.println(">>>>>>>>>>>>>>>>Controller方法被调用时>>>>>>>>>>>>>>>>>>>>>");
        showBindMethods(handlerAdapter);
        System.out.println(">>>>>>>>>>>>>>>>Controller方法被调用时>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void showBindMethods(RequestMappingHandlerAdapter handlerAdapter) throws Exception {
        Field initBinderAdviceCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderAdviceCache");
        Field initBinderCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderCache");
        initBinderCache.setAccessible(true);
        initBinderAdviceCache.setAccessible(true);
        Map<ControllerAdviceBean, Set<Method>> map =  (Map<ControllerAdviceBean, Set<Method>>)initBinderAdviceCache.get(handlerAdapter);


        map.forEach((k, v) -> {
            v.forEach(item -> {
                System.out.println("全局的：" + k.getBeanType().getSimpleName() + "->" +item.getName());
            });
        });


        Map<Class< ? >, Set<Method>> map1 =  (Map<Class< ? >, Set<Method>>)initBinderCache.get(handlerAdapter);
        map1.forEach((k, v) -> {
            v.forEach(item -> {
                System.out.println("Controller独有的：" + k.getSimpleName() + "->" +item.getName());
            });
        });
    }
}

