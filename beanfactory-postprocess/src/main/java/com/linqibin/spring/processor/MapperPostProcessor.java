package com.linqibin.spring.processor;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;

/**
 * @Author linqibin
 * @Date 2023/8/26 10:04
 * @Email 1214219989@qq.com
 */
public class MapperPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            // 只扫描指定包下的Mapper， 例如com.linqibin.spring.mapper。   参考@MapperScan
            PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory(patternResolver);
            // 获取资源
            Resource[] resources = patternResolver.getResources("classpath*:/com/linqibin/spring/mapper/**/*.class");
            AnnotationBeanNameGenerator nameGenerator = new AnnotationBeanNameGenerator();
            for (Resource resource : resources) {
                MetadataReader reader = factory.getMetadataReader(resource);
                ClassMetadata classMetadata = reader.getClassMetadata();
                // 只处理接口
                if (classMetadata.isInterface()) {
                    // 使用MapperFactoryBean动态生成
                    AbstractBeanDefinition bd = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class)
                            .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                            // 为什么使用classMetadata.getClassName()就可以了， MapperFactoryBean的构造函数传的不是Class吗？？？
                            // 查看源码后发现Spring根据类的构造， 自动转换。牛逼！！
                            .addConstructorArgValue(classMetadata.getClassName())
                            .getBeanDefinition();
                    // 源码中生成bean的名字也是这么做的
                    AbstractBeanDefinition nameBd = BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName())
                            .getBeanDefinition();
                    String beanName = nameGenerator.generateBeanName(nameBd, registry);

                    registry.registerBeanDefinition(beanName, bd);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}

