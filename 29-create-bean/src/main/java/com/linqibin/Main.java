package com.linqibin;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application.xml");
        Student bean01 = applicationContext.getBean("student1", Student.class);
        System.out.println("第八步:使用Bean" + bean01);
        applicationContext.close();
    }
}