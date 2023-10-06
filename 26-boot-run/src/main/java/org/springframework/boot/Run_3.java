package org.springframework.boot;

import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.EnvironmentPostProcessorApplicationListener;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class Run_3 {

    public static void main(String[] args) throws IOException {
        SpringApplication application = new SpringApplication(Run_3.class);
        // 3. 准备Springboot运行环境信息
        ApplicationEnvironment environment = new ApplicationEnvironment();  // 保存了系统环境变量，属性变量，yaml等中的键值对信息
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            System.out.println(propertySource);
        }
        System.out.println(environment.getProperty("JAVA_HOME"));

        // 将命令行传递的键值对作为首选的属性
        environment.getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        // 在命令行传递server.port后，获取的就是命令行的server.port了。
        System.out.println(environment.getProperty("server.port"));

        // 4. 将所有键用-连接,读取各种不规则命名的key
        ConfigurationPropertySources.attach(environment);
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            System.out.println(propertySource);
        }

        // 5. 通过后置处理器添加一些属性的源
//        ConfigDataEnvironmentPostProcessor postProcessor = new ConfigDataEnvironmentPostProcessor(new DeferredLogs(), new DefaultBootstrapContext());
//        postProcessor.postProcessEnvironment(environment, application);
//        System.out.println(">>>>>>>>>通过后置处理器添加后源包括：");
//        for (PropertySource<?> propertySource : environment.getPropertySources()) {
//            System.out.println(propertySource.getName());
//        }
        // spring通过spring.factory添加了一系列的environmentPostprocessor
        application.addListeners(new EnvironmentPostProcessorApplicationListener());
        EventPublishingRunListener eventPublish = new EventPublishingRunListener(application, args);
        eventPublish.environmentPrepared(new DefaultBootstrapContext(), environment);
        System.out.println(">>>>>>>>>通过后置处理器添加后源包括：");
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            System.out.println(propertySource.getName());
        }

        // 6.将environment中前缀为spring.main的属性绑定到application中。
        environment.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("step6.properties")));
        Binder.get(environment).bind("spring.main", Bindable.ofInstance(application));   // 通过debug可以看到属性变化。
    }
}
