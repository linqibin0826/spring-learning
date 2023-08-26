package com.linqibin.spring.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author linqibin
 * @Date 2023/8/26 14:49
 * @Email 1214219989@qq.com
 */
@Component
public class SingletonObject {

    @Lazy
    @Autowired
    private ProtoObject protoObject;

    public ProtoObject getProtoObject() {
        return protoObject;
    }

}

