package com.linqibin.spring.aware;

import com.linqibin.spring.aware.component.MyBean1;
import com.linqibin.spring.aware.component.MyBean2;
import com.linqibin.spring.aware.config.MyConfig;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @Author linqibin
 * @Date 2023/8/26 12:29
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();

        // 使用各种aware接口去注入指定对象。
        context.registerBean(MyBean1.class);
        // 直接在类中使用@Autowired方式注入（某些情况下会失效）。比如MyConfig类中的那样，
        // 因为使用@autowired需要使用BeanPostProcessor去解析，但MyConfig由于包含注册了BeanFactoryPostProcessor，被提前创建，此时refresh方法中
        // 还没有走到registerBeanPostProcessors(), 因此没有@Autowired的解析器。所以没法注入。  虽然使用ApplicationContextAware接口，也是通过BeanPostProcessor的方式，
        // 但是这个处理器是在prepareBeanFactory()时就已经添加了~~~，因此能解析。
        context.registerBean(MyBean2.class);
        // 可以看到，两个对象并没有被成功注入（因为这个配置类，注册了BeanFactoryPostProcessor，被提前创建了）
        context.registerBean(MyConfig.class);


        // 手动注册BeanFactory后置处理器，refresh的时候会根据顶级接口（BeanFactoryPostProcessor）去找容器中找到，并执行他们的重写方法
//        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());

        context.refresh();

        for (String definitionName : context.getBeanDefinitionNames()) {
            System.out.println(definitionName);
        }
    }
}

