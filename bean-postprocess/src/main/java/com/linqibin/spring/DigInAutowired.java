package com.linqibin.spring;

import com.linqibin.spring.component.Bean01;
import com.linqibin.spring.component.Bean02;
import com.linqibin.spring.component.Bean03;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 分析处理@Autowired注解解析的后置处理器
 *
 * @Author linqibin
 * @Date 2023/8/23 22:11
 * @Email 1214219989@qq.com
 */
public class DigInAutowired {

    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        Bean01 bean01 = new Bean01();
        beanFactory.registerSingleton("bean01", bean01);
        Bean02 bean02 = new Bean02();
        beanFactory.registerSingleton("bean02", bean02);
        System.out.println("容器中的Bean02~：" + bean02);

        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);
        // 容器现在有三个单例的bean，都还没有被注入，我们来解析一下@Autowired

        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        // 要给bean对象注入容器内的其他对象， 那肯定需要从容器中去getBean(), 因此我们需要设置一下processor的BeanFactory
        processor.setBeanFactory(beanFactory);

        // 1.查找哪些方法加了@Autowired， 这称之为InjectionMetadata
        Method method = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        method.setAccessible(true);
        InjectionMetadata injectionMetadata = (InjectionMetadata) method.invoke(processor, null, Bean03.class, null);

        // 2.调用InjectionMetadata来进来依赖注入，注入时按类型查找值
        Bean03 bean03 = new Bean03();
        injectionMetadata.inject(bean03, null, null);

        // 3.@Value虽然生效了，但是里面的值好像并没有被正确解析。我们到前面创建beanFactory时给它指定ValueResolver。代码如下
        // beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);

        /*
         4.最后，探索一下inject里面是如何根据类型找到对应的Bean。在执行inject的时候，会根据element去生成DependencyDescriptor对象，
         然后再由beanFactory去注入指定类型的bean
         比如，我在Bean03中添加了一个Bean02对象，名称叫做injectTest，通过@Autowired注入
         */
        Field injectTest = Bean03.class.getDeclaredField("injectTest");
        injectTest.setAccessible(true);
        DependencyDescriptor descriptor = new DependencyDescriptor(injectTest, true);
        // 通过beanFactory解析
        Object done = beanFactory.doResolveDependency(descriptor, null, null, null);
        System.out.println("根据类型从BeanFactory中找出来Bean02是否与一开始注册到容器中的一致：" + (done == bean02));


        // 通过对比可以发现，找出来的就是容器中的Bean02

        Method setBean01 = Bean03.class.getDeclaredMethod("setBean01", Bean01.class);
        DependencyDescriptor methodDescriptor = new DependencyDescriptor(new MethodParameter(setBean01, 0), true);
        Object resolved = beanFactory.doResolveDependency(methodDescriptor, null, null, null);
        System.out.println("根据类型从BeanFactory中找出来Bean01是否与一开始注册到容器中的一致：" + (resolved == bean01));
    }
}