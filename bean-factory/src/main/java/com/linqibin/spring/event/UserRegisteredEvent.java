package com.linqibin.spring.event;

import org.springframework.context.ApplicationEvent;

/**
 * 用户已注册事件
 *
 * @Author linqibin
 * @Date 2023/8/20 14:00
 * @Email 1214219989@qq.com
 */
public class UserRegisteredEvent extends ApplicationEvent {

    /**
     * 用户手机号码
     */
    private final String phone;

    public UserRegisteredEvent(Object source, String phone) {
        super(source);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}

