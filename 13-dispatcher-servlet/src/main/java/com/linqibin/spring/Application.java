package com.linqibin.spring;

import com.linqibin.spring.config.WebConfig;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

/**
 * DispatcherServlet初始化时机
 *
 * @Author linqibin
 * @Date 2023/8/30 22:45
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) throws Exception {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        // 获取解析后的映射结果
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((key, value) -> System.out.println(key + "=" + value));

        // 调用请求，获取到处理器执行连
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test/get");
        HandlerExecutionChain chain = handlerMapping.getHandler(request);
        System.out.println(chain);

        // 通过处理器适配器去调用  处理器（方法逻辑）
        RequestMappingHandlerAdapter handlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        handlerAdapter.handle(request, response, chain.getHandler());
    }



}

