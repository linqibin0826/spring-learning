package com.linqibin.run;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Arrays;

@Configuration
public class Run {

    @SuppressWarnings("all")
    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Run.class);

        // 通过函数式接口的方式将代码的调用推迟了
        application.addInitializers(context -> System.out.println("ApplicationContextInitializers 被调用了~~~"));

        // 2.封装args
        DefaultApplicationArguments arguments = new DefaultApplicationArguments(args);

        // 8.创建容器
        GenericApplicationContext context = createApplicationContext(WebApplicationType.SERVLET);
        System.out.println("创建容器成功~~");
        // 9.准备容器(将前面添加的应用容器初始化器在这边进行初始化操作
        for (ApplicationContextInitializer initializer : application.getInitializers()) {
            initializer.initialize(context);
        }
        // 10.加载bean定义(application.setSources(new HashSet<>()))
        // 10.1 根据注解扫描
        AnnotatedBeanDefinitionReader bdr = new AnnotatedBeanDefinitionReader(context.getDefaultListableBeanFactory());
        bdr.registerBean(Config.class);
        // 10.2 指定包扫描
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context.getDefaultListableBeanFactory());
        scanner.scan("com.linqibin.run");
        // 10.3 根据配置文件扫描
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context.getDefaultListableBeanFactory());
        xmlReader.loadBeanDefinitions(new ClassPathResource("bean.xml"));

        // 11.refresh容器
        System.out.println(">>>>>>>>>>>>>>>>>刷新容器");
        context.refresh();

        for (String definitionName : context.getBeanDefinitionNames()) {
            System.out.println(definitionName + "->" + context.getBeanDefinition(definitionName).getResourceDescription());
        }

        // 12.执行runner（在Spring加载的最后阶段被回调，可以通过这个机制做一些业务逻辑，比如预加载一些数据，两种runner主要区别在于参数）
        System.out.println(">>>>>>>>>>>>>>>>>执行runner");
        for (CommandLineRunner commandLineRunner : context.getBeansOfType(CommandLineRunner.class).values()) {
            commandLineRunner.run(args);
        }

        for (ApplicationRunner applicationRunner : context.getBeansOfType(ApplicationRunner.class).values()) {
            applicationRunner.run(arguments);
        }
    }

    /**
     * 根据应用类型创建不同的spring容器
     *
     * @param applicationType 应用类型
     * @return spring容器
     */
    private static GenericApplicationContext createApplicationContext(WebApplicationType applicationType) {
        switch (applicationType) {
            case SERVLET:
                return new AnnotationConfigServletWebServerApplicationContext();
            case REACTIVE:
                return new AnnotationConfigReactiveWebServerApplicationContext();
            case NONE:
                return new AnnotationConfigApplicationContext();
        }
        return null;
    }

    public static class Bean01 {

    }

    public static class Bean02 {

    }

    @Configuration
    public static class Config {

        @Bean
        public Bean01 bean01() {
            return new Bean01();
        }

        @Bean
        public DispatcherServlet dispatcherServlet() {
            return new DispatcherServlet();
        }

        @Bean
        public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public DispatcherServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        @Bean
        public CommandLineRunner commandLineRunner() {
            return args -> System.out.println(Arrays.toString(args));
        }

        @Bean
        public ApplicationRunner applicationRunner() {
            return args -> {
                // 原始参数，作用同commandLineRunner
                System.out.println(Arrays.toString(args.getSourceArgs()));
                // 获取所有可选参数名称
                System.out.println("ApplicationRunner中的可选参数列表：" + args.getOptionNames());
                // 获取所有不带--的参数
                System.out.println("ApplicationRunner中的非可选参数列表：" + args.getNonOptionArgs());
            };
        }
    }
}
