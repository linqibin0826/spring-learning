package com.linqibin;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Data
public class Student implements ApplicationContextAware, InitializingBean, DisposableBean {

    private String name;

    public Student() {
        System.out.println("第一步:对象实例化");
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("第二步:依赖注入阶段");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("第三步:一系列的Aware接口");
    }



    public void init() {
        System.out.println("第六步:初始化方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("第五步:InitializingBean中的afterPropertiesSet方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("第九步:销毁前调用的方法");
    }

    public void destroyMethod() {
        System.out.println("第十步:销毁方法");
    }
}
