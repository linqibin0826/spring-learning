package com.linqibin;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        // 解析配置类
        context.registerBean("config", MyConfig.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.setAllowBeanDefinitionOverriding(false);

        context.refresh();

        for (String definitionName : context.getBeanDefinitionNames()) {
            System.out.println(definitionName);
        }
        Bean01 bean = context.getBean(Bean01.class);
        System.out.println("被注入到容器中的bean名称为：" + bean.getName());
    }
}

@Import(MyImportSelector.class)
@Configuration
class MyConfig {
    @Bean
    public Bean01 bean01() {
        return new Bean01("本项目Bean01");
    }
}


class MyImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> factoryNames = SpringFactoriesLoader.loadFactoryNames(MyImportSelector.class, null);
        return factoryNames.toArray(new String[0]);
    }
}

@Data
@AllArgsConstructor
class Bean01 {
    private String name;
}

/**
 * 第三方的配置类1
 */
@Configuration
class Config1 {
    @Bean
    @ConditionalOnMissingBean(Bean01.class)
    public Bean01 bean01() {
        return new Bean01("第三方Bean01");
    }
}

/**
 * 第三方的配置类2
 */
@Configuration
class Config2 {

}