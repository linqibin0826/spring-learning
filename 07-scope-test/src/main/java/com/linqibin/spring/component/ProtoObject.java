package com.linqibin.spring.component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author linqibin
 * @Date 2023/8/26 14:51
 * @Email 1214219989@qq.com
 */
// 注入使用代理对象
@Scope(value = "prototype")
@Component
public class ProtoObject {

    public void hello() {
        System.out.println(this);
    }
}

