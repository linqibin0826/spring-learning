package com.linqibinl;

import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

import java.lang.reflect.Method;

/**
 * 演示Springboot启动过程
 * @author linqibin
 * @date 2023/9/12 23:03
 * @email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) throws Exception {

        // 【构造方法】
        // 1.获取BeanDefinition主源
        SpringApplication application = new SpringApplication();
        application.addPrimarySources(Lists.newArrayList(Application.class));

        // 2.推断应用类型(通过判断类路径下面存不存在某些类【DispatcherServlet】)
        Method deduceFromClasspath = WebApplicationType.class.getDeclaredMethod("deduceFromClasspath");
        deduceFromClasspath.setAccessible(true);
        WebApplicationType applicationType = (WebApplicationType) deduceFromClasspath.invoke(null);
        System.out.println(applicationType);

        // 3.
    }
}

