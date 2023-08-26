package com.linqibin.spring.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

/**
 * 处理@Bean注解
 *
 * @Author linqibin
 * @Date 2023/8/25 22:43
 * @Email 1214219989@qq.com
 */
public class AtBeanPostProcessor implements BeanFactoryPostProcessor, BeanDefinitionRegistryPostProcessor {
    private BeanDefinitionRegistry registry;

    /**
     * 这里直接写死一个配置类，我们只演示从这个类里面加载@Bean
     */
    private static final String CONFIG_CLASS_NAME = "com/linqibin/spring/config/BeanConfig.class";



    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        try {
            // 不走类加载直接获取到类元信息
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(new ClassPathResource(CONFIG_CLASS_NAME));
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            // 获取被标注了某个注解的方法
            Set<MethodMetadata> methods = annotationMetadata.getAnnotatedMethods(Bean.class.getName());
            methods.forEach(method -> {
                // 将方法注册到BeanDefinition
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
                // factoryBean就是这个方法所在的@Configuraion的那个类Bean名称。 用这个类来调用这些方法来创建Bean。
                builder.setFactoryMethodOnBean(method.getMethodName(), "beanConfig");
                // 设置Bean的自动注入模式为构造注入。
                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                // 注册初始化方法
                String initMethod = method.getAnnotationAttributes(Bean.class.getName()).get("initMethod").toString();
                builder.setInitMethodName(initMethod);
                AbstractBeanDefinition bd = builder.getBeanDefinition();
                if (configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
                    DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                    // @Bean一般使用方法名称作为bean名字
                    beanFactory.registerBeanDefinition(method.getMethodName(), bd);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }
}

