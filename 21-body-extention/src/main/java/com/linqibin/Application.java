package com.linqibin;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author linqibin
 * @date 2023/9/9 17:55
 * @email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);


        ServletInvocableHandlerMethod invocableHandlerMethod = new ServletInvocableHandlerMethod(new WebConfig.Controller1(),
                WebConfig.Controller1.class.getMethod("user", String.class));

        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        // 初始化ControllerAdvice解析
        handlerAdapter.setApplicationContext(context);
        handlerAdapter.afterPropertiesSet();
        // 1.设置参数解析器
        Method getDefaultArgumentResolvers = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDefaultArgumentResolvers");
        getDefaultArgumentResolvers.setAccessible(true);
        List<HandlerMethodArgumentResolver> argumentResolvers = (List<HandlerMethodArgumentResolver>) getDefaultArgumentResolvers.invoke(handlerAdapter);
        HandlerMethodArgumentResolverComposite argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
        argumentResolverComposite.addResolvers(argumentResolvers);
        invocableHandlerMethod.setHandlerMethodArgumentResolvers(argumentResolverComposite);

        // 2.设置返回值处理器
        Method getDefaultReturnValueHandlers = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDefaultReturnValueHandlers");
        getDefaultReturnValueHandlers.setAccessible(true);

        // 2.1.设置messageConverter，把对象转换成json
        handlerAdapter.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        List<HandlerMethodReturnValueHandler> returnValueHandlers = (List<HandlerMethodReturnValueHandler>) getDefaultReturnValueHandlers.invoke(handlerAdapter);
        HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
        returnValueHandlerComposite.addHandlers(returnValueHandlers);
        invocableHandlerMethod.setHandlerMethodReturnValueHandlers(returnValueHandlerComposite);

        // 3.创建请求、响应对象
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("linqibin".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        invocableHandlerMethod.invokeAndHandle(webRequest, new ModelAndViewContainer());

        System.out.println(response.getContentAsString(StandardCharsets.UTF_8));
    }
}

