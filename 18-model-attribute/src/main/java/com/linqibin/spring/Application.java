package com.linqibin.spring;

import com.linqibin.spring.config.WebConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.lang.reflect.Method;

/**
 * 测试通过全局的@ModelAttribute向模型中添加数据
 * @Author linqibin
 * @Date 2023/9/7 23:14
 * @Email 1214219989@qq.com
 */
public class Application {


    public static void main(String[] args) throws Exception {
        // 复用一下前一节的代码

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        // 手动初始化一个
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setApplicationContext(context);
        handlerAdapter.afterPropertiesSet();

        // 1.初始化时，指定Controller的bean， 和对应的方法
        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(new WebConfig.Controller1(), WebConfig.Controller1.class.getMethod("test", WebConfig.User.class));

        // 2.设置WebDataBinderFactory
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);
        handlerMethod.setDataBinderFactory(factory);

        // 3.设置ParameterNameDiscover
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.setParameterNameDiscoverer(discoverer);

        // 4.设置方法的参数解析器组合
        HandlerMethodArgumentResolverComposite argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
        argumentResolverComposite.addResolvers(new ServletModelAttributeMethodProcessor(true));
        handlerMethod.setHandlerMethodArgumentResolvers(argumentResolverComposite);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "hugh");
        ServletWebRequest webRequest = new ServletWebRequest(request);

        ModelAndViewContainer container = new ModelAndViewContainer();


        // 5.初始化ModelFactory, 每个请求被调用都会返回一个ModelFactory
        Method getModelFactory = handlerAdapter.getClass().getDeclaredMethod("getModelFactory", HandlerMethod.class, WebDataBinderFactory.class);
        getModelFactory.setAccessible(true);
        ModelFactory modelFactory = (ModelFactory) getModelFactory.invoke(handlerAdapter, handlerMethod, factory);
        modelFactory.initModel(webRequest, container, handlerMethod);

        handlerMethod.invokeAndHandle(webRequest, container);
        System.out.println(container.getModel());
    }
}

