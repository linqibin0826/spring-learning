package com.linqibin.spring.component;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 使用ObjectFactory来注入原型
 * @Author linqibin
 * @Date 2023/8/26 15:15
 * @Email 1214219989@qq.com
 */
@Component
public class ObjectFactoryTest {

    @Autowired
    private ObjectFactory<ProtoObject> protoFactory;

    public ProtoObject getProtoObject() {
        return protoFactory.getObject();
    }
}

