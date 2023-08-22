package com.linqibin.spring.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author linqibin
 * @Date 2023/8/22 21:53
 * @Email 1214219989@qq.com
 */
public class Bean03 {

    private Bean01 bean01;

    private Bean02 bean02;

    private String home;

    public Bean03() {
        System.out.println("Bean03被实例化~~~~");
    }

    @Autowired
    private void setBean01(Bean01 bean01) {
        this.bean01 = bean01;
        System.out.println("@autowired生效~~~~" + bean01);
    }

    @Resource
    private void setBean02(Bean02 bean02) {
        this.bean02 = bean02;
        System.out.println("@Resource生效~~~" + bean02);
    }

    @Autowired
    private void setHome(@Value("${JAVA_HOME}") String home) {
        this.home = home;
        System.out.println("@Value生效" + home);
    }

    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct生效~~~~~");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("@PreDestroy生效~~~~~");
    }
}

