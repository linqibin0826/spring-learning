package com.linqibin.spring;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟Spring中BeanFactory使用模板方法
 * 模板方法分成动和静两个部分，静的部分是固定的流程，就比如Bean的各个阶段是不会变的（实例化，依赖注入，初始化，销毁）
 * 动的部分比如依赖注入的方式，支持多种：@Autowired，@Value，@Resource等，以后还会继续添加，所以这就是动的部分，
 * 需要抽象出来
 *
 * @Author linqibin
 * @Date 2023/8/22 19:25
 * @Email 1214219989@qq.com
 */
public class TestMethodTemplate {

    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        beanFactory.addPostProcessor(bean -> System.out.println("@Autowired解析"));

        beanFactory.addPostProcessor(bean -> System.out.println("@Resource解析"));

        beanFactory.getBean();
    }

    static class MyBeanFactory {

        List<BeanPostProcessor> postProcessors = new ArrayList<>();

        // 模拟bean的创建过程
        public Object getBean() {
            // 实例化
            Object bean = new Object();
            System.out.println("构造~" + bean);
            System.out.println("依赖注入" + bean);
            for (BeanPostProcessor processor : postProcessors) {
                processor.postProcess(bean);
            }
            System.out.println("初始化" + bean);
            System.out.println("销毁" + bean);
            return bean;
        }

        public void addPostProcessor(BeanPostProcessor postProcessor) {
            this.postProcessors.add(postProcessor);
        }
    }

    interface BeanPostProcessor{
        void postProcess(Object bean);
    }
}

