package com.linqibin.spring.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 测试BeanFactory的实现类
 *
 * @Date 2023/8/20 15:20
 */
@Slf4j
public class FactoryImplApplication {

    public static void main(String[] args) {

        // 1.仅创建BeanFactory，并没有创建ApplicationContext，此时打印容器中的BeanDefinition个数，一个都没有。
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        log.info("仅创建beanFactory时，不会自动创建其他的BeanDefinition，此时BeanDefinition个数为：{}", beanFactory.getBeanDefinitionCount());

        // 2.手动创建Config.class的BeanDefinition并注册到BeanFactory中。
        AbstractBeanDefinition configBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class)
                .setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config", configBeanDefinition);
        log.info("向BeanFactory手动注册一个BeanDefinition，此时BeanDefinition个数为：{}", beanFactory.getBeanDefinitionCount());

        // 3.学过Spring的应该都知道，Config类上@Configuration，并且里面的方法上有@Bean，那么这个方法的返回对象应该也会被注入容器中。但这边为什么没有呢？
        // 这是因为@Configuration并没有被解析，它是由后置处理器来处理的（ConfigurationClassPostProcessor）。主要功能是补充了一些BeanDefinition，
        // 接着，我们给它添加一些常用的后置处理器，重新打印日志。
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        log.info("向BeanFactory添加一些常用的BeanFactory后置处理器后，此时BeanDefinition个数为：{}", beanFactory.getBeanDefinitionCount());
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).forEach((key, value) -> {
            // 调用后置处理器
            value.postProcessBeanFactory(beanFactory);
        });
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);

        // 4. 有了BeanDefinition之后，就可以获取Bean了，可以成功得到bean01，但是bean02好像并没有被依赖注入
        // 这是因为创建bean01之后，beanFactory并不会主动依赖注入，还需要添加Bean后置处理器进行处理。
        // 由于第3步注册过BeanDefinition了（registerAnnotationConfigProcessors），我们现在只需要将它添加到beanFactory
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);

        beanFactory.preInstantiateSingletons();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Component01 bean01 = beanFactory.getBean(Component01.class);
        System.out.println("从容器中获取的bean01：" + bean01);
        System.out.println("被依赖注入的bean02：" + bean01.getBean02());

    }

    @Configuration
    static class Config{

        @Bean
        public Component01 bean1() {
            return new Component01();
        }

        @Bean
        public Component02 bean2() {
            return new Component02();
        }
    }

    static class Component01 {

        @Resource
        private Component02 bean02;

        public Component01() {
            System.out.println("Component01构造器~~~");
        }

        public Component02 getBean02() {
            return bean02;
        }
    }

    static class Component02 {

        public Component02() {
            System.out.println("Component02构造器~~~");
        }
    }
}

