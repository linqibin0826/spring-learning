package com.linqibin.spring;

import com.linqibin.spring.component.ObjectFactoryTest;
import com.linqibin.spring.component.ProtoObject;
import com.linqibin.spring.component.SingletonObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 单例对象注入其他scope的对象
 * @Author linqibin
 * @Date 2023/8/26 14:48
 * @Email 1214219989@qq.com
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        SingletonObject singletonObject = (SingletonObject)context.getBean("singletonObject");


        // 在创建singletonObject的时候，由于注入只发生一次，所以获取到的目标对象一直都是同一个对象。
        // 如果想要得到的对象不是同一个，需要使用@Lazy或者在目标对象的scope添加属性，proxyMode，设置为代理对象。 @Lazy的原理也是代理对象
        ProtoObject protoObject = singletonObject.getProtoObject();

        // 好像调用一次目标对象任何方法，就会生成一个目标对象。不知道是什么意义？？？？
        protoObject.hello();
        protoObject.hello();

        // 方式三：通过ObjectFactory。 这种方式就解决了上诉的问题了。。。因为我们得到的并不是代理对象了。
        ObjectFactoryTest factoryTest = (ObjectFactoryTest) context.getBean("objectFactoryTest");
        ProtoObject object = factoryTest.getProtoObject();
        object.hello();
        object.hello();

        // 虽然各个解决方式不同，但殊途同归， 都是延迟scope Bean的获取，不在创建这个单例对象的时候，就直接注入。
    }
}

