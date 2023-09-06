package com.linqibin;

import lombok.Data;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @Author linqibin
 * @Date 2023/9/6 22:04
 * @Email 1214219989@qq.com
 */
public class BeanWrapperImplTest {

    public static void main(String[] args) {
        MyBean target = new MyBean();
        BeanWrapperImpl wrapper = new BeanWrapperImpl(target);
        wrapper.setPropertyValue("username", "linqibin");
        wrapper.setPropertyValue("password", "123");
        System.out.println(target);
    }

    @Data
    static class MyBean {
        private String username;

        private String password;
    }
}

