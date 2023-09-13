package com.linqibin;

import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Method;

/**
 * 演示Springboot启动过程
 *
 * @author linqibin
 * @date 2023/9/12 23:03
 * @email 1214219989@qq.com
 */
@Configuration
public class Application {

    public static void main(String[] args) throws Exception {

        // 【构造方法】
        // 1.获取BeanDefinition主源
        SpringApplication application = new SpringApplication(Application.class);
        // 为容器设置更多的源
        application.setSources(Sets.newHashSet("classpath:bean01.xml"));

        // 2.推断应用类型(通过判断类路径下面存不存在某些类【DispatcherServlet】)
        Method deduceFromClasspath = WebApplicationType.class.getDeclaredMethod("deduceFromClasspath");
        deduceFromClasspath.setAccessible(true);
        WebApplicationType applicationType = (WebApplicationType) deduceFromClasspath.invoke(null);
        System.out.println("推断出来的应用类型是：" +  applicationType);

        // 3.加载spring.factories下的ApplicationContextInitializer（这边只是添加，等创建之后会回调），可用来添加一些beanDefinition信息。
        application.setInitializers(Sets.newHashSet(
                (ApplicationContextInitializer<ConfigurableApplicationContext>) applicationContext -> {
                    GenericApplicationContext gac = (GenericApplicationContext) applicationContext;
                    gac.registerBeanDefinition("bean03", BeanDefinitionBuilder.genericBeanDefinition(Bean03.class).getBeanDefinition());
                })
        );

        // 4.加载spring.factories下的ApplicationListener（注册监听器，等待回调）,监听Spring容器中的全部事件
        application.addListeners(event -> System.out.println("收到新事件，事件名称：" + event.getClass()));

        // 5.推断main方法所在的类
        Method deduceMainApplicationClass = SpringApplication.class.getDeclaredMethod("deduceMainApplicationClass");
        deduceMainApplicationClass.setAccessible(true);
        Class<?> mainClass = (Class<?>)deduceMainApplicationClass.invoke(application);
        System.out.println("main方法所在的类是：" + mainClass);

        ConfigurableApplicationContext context = application.run(args);
        for (String definitionName : context.getBeanDefinitionNames()) {
            System.out.println(definitionName + "->" + context.getBeanFactory().getBeanDefinition(definitionName).getResourceDescription());
        }

        context.close();
    }

    @Data
    public static class Bean01 {
        private String name;
    }

    @Data
    public static class Bean02 {
        private String filename;
    }

    @Data
    public static class Bean03 {
        private String detail;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public Bean02 bean02() {
        return new Bean02();
    }
}

