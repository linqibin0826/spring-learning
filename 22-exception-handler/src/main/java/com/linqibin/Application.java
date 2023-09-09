package com.linqibin;

import org.assertj.core.util.Lists;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author linqibin
 * @date 2023/9/9 20:34
 * @email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        HandlerMethod handlerMethod = new HandlerMethod(new WebConfig.Controller1(), WebConfig.Controller1.class.getMethod("test01"));

        NullPointerException exception = new NullPointerException("发生空指针异常");
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        // 设置MessageConverter
        resolver.setMessageConverters(Lists.newArrayList(new MappingJackson2HttpMessageConverter()));
        // 此方法会初始化参数解析器和返回值处理器
        resolver.setApplicationContext(context);
        resolver.afterPropertiesSet();


        MockHttpServletResponse response = new MockHttpServletResponse();
        resolver.resolveException(new MockHttpServletRequest(), response, handlerMethod, exception);
        System.out.println(response.getContentAsString(StandardCharsets.UTF_8));
    }
}

