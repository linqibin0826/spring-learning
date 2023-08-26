package com.linqibin.spring.component;

import com.linqibin.spring.event.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 完成用户注册逻辑
 *
 * @Author linqibin
 * @Date 2023/8/20 11:30
 * @Email 1214219989@qq.com
 */
@Component
@Slf4j
public class Component01 {

    @Resource
    private ApplicationEventPublisher eventPublisher;

    public void handleUserRegistered() {
        log.info("用户注册~");
        // 发布用户已注册事件
        eventPublisher.publishEvent(new UserRegisteredEvent(this, "119"));
    }
}

