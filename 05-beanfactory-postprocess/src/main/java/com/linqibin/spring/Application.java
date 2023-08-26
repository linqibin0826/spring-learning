package com.linqibin.spring;

import com.linqibin.spring.config.BeanConfig;
import com.linqibin.spring.processor.AtBeanPostProcessor;
import com.linqibin.spring.processor.ComponentScanPostProcessor;
import com.linqibin.spring.processor.MapperPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;

/**
 * 用来测试BeanFactoryPostProcessor
 * @Author linqibin
 * @Date 2023/8/24 23:05
 * @Email 1214219989@qq.com
 */
public class Application {



    public static void main(String[] args) throws Exception {

        // 干净的容器
        GenericApplicationContext context = new GenericApplicationContext();

        // 手动注册了一个Bean，但是里面的其他Bean肯定不会生效，因为那些注解都没有被解析。
        context.registerBean("beanConfig", BeanConfig.class);

        // 我们可以使用Spring的BeanFactoryPostProcessor来解析。
//        context.registerBean(ConfigurationClassPostProcessor.class);
//        context.registerBean(MapperScannerConfigurer.class,
//                bd -> bd.getPropertyValues()
//                        .addPropertyValue("basePackage", "com.linqibin.spring.mapper")
//        );

        // 现在我们自己简单实现一下，去处理这里面的一些功能， 就是把BeanConfig里面的Bean也加入到BeanDefinition
        context.registerBean(ComponentScanPostProcessor.class);

        // 处理@Bean的后置处理器
        context.registerBean(AtBeanPostProcessor.class);

        // 动态生成Mapper接口的Bean对象
        context.registerBean(MapperPostProcessor.class);

        // 刷新容器
        context.refresh();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
    }

   }

