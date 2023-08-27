package com.linqibin.spring.processor;

import com.linqibin.spring.config.BeanConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CoponentScan的BeanFactoryPostProcessor
 *
 * @Author linqibin
 * @Date 2023/8/25 21:53
 * @Email 1214219989@qq.com
 */
public class ComponentScanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private static final String RESOURCE_PATTERN = "/**/*.class";

    private BeanDefinitionRegistry beanDefinitionRegistry;


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            AnnotationBeanNameGenerator nameGenerator = new AnnotationBeanNameGenerator();

            ComponentScan componentScan = AnnotationUtils.findAnnotation(BeanConfig.class, ComponentScan.class);
            if (componentScan != null) {
                for (String basePackage : componentScan.basePackages()) {
                    // 指定基本包下面的所有.class文件
                    String path = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage.replace(".", "/") + RESOURCE_PATTERN;
                    Resource[] resources = resourcePatternResolver.getResources(path);
                    for (Resource resource : resources) {
                        // 判断每个资源下是否有@Component注解，或者其派生的注解
                        MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
                        ClassMetadata classMetadata = reader.getClassMetadata();
                        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
                        if (annotationMetadata.hasAnnotation(Component.class.getName())
                                || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                            // 添加BeanDefinition信息
                            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName()).getBeanDefinition();
                            // 生成bean的名称
                            String beanName = nameGenerator.generateBeanName(beanDefinition, beanDefinitionRegistry);
                            beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.beanDefinitionRegistry = registry;
    }
}

