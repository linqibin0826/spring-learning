package com.linqibin.spring;

import com.linqibin.spring.component.Bean01;
import com.linqibin.spring.component.Bean02;
import com.linqibin.spring.component.Bean03;
import com.linqibin.spring.component.Bean04;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

/**
 * 测试往容器中添加一些Bean后置处理器时的作用
 *
 * @Author linqibin
 * @Date 2023/8/22 21:51
 * @Email 1214219989@qq.com
 */
public class TestProcessors {

    public static void main(String[] args) {
        // 通用ApplicationContext，这是一个比较【干净】的容器，可以用来测试手动添加后置处理器的效果
        GenericApplicationContext context = new GenericApplicationContext();

        // 往容器中添加三个Bean，默认是单例的，其中Bean03比较复杂，Bean01、Bean02中什么也没有
        context.registerBean("bean01", Bean01.class);
        context.registerBean("bean02", Bean02.class);
        context.registerBean("bean03", Bean03.class);

        // >>>>>>>>>添加BeanPostProcessor<<<<<<<<<<

        // 因为涉及到注入String对象，所以需要换掉候选解析器（先看个眼熟，后续讲）
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        // 添加后，@Autowired、@Value会生效
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);

        // 添加后，@Resource、@PostConstruct、@PreDestroy会生效
        context.registerBean(CommonAnnotationBeanPostProcessor.class);


        // 添加后，@ConfigurationProperties会生效
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());
        context.registerBean("bean04", Bean04.class);

        // 初始化容器（也就是让ApplicationContext帮我们去执行BeanFactory的一系列流程）
        context.refresh();
        System.out.println(context.getBean("bean04"));

        context.close();
    }


}

