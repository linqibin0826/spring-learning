package com.linqibin.spring.component;

import com.linqibin.spring.event.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 事件监听器
 *
 * @Author linqibin
 * @Date 2023/8/20 11:30
 * @Email 1214219989@qq.com
 */
@Component
@Slf4j
public class Component02 {

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("发送短信给用户：{}了~", event.getPhone());
    }
}

