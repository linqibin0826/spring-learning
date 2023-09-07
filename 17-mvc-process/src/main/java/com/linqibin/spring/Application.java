package com.linqibin.spring;

import com.linqibin.spring.config.WebConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import static com.linqibin.spring.config.WebConfig.Controller1;
import static com.linqibin.spring.config.WebConfig.User;

/**
 * SpringMVC方法执行流程模拟
 *
 * @Author linqibin
 * @Date 2023/9/7 22:27
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        // 1.初始化时，指定Controller的bean， 和对应的方法
        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(new Controller1(), Controller1.class.getMethod("test", User.class));

        // 2.设置WebDataBinderFactory
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);
        handlerMethod.setDataBinderFactory(factory);

        // 3.设置ParameterNameDiscover
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.setParameterNameDiscoverer(discoverer);

        // 4. 设置方法的参数解析器组合
        HandlerMethodArgumentResolverComposite argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
        argumentResolverComposite.addResolvers(new ServletModelAttributeMethodProcessor(true));
        handlerMethod.setHandlerMethodArgumentResolvers(argumentResolverComposite);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "hugh");

        ModelAndViewContainer container = new ModelAndViewContainer();
        handlerMethod.invokeAndHandle(new ServletWebRequest(request), container);

        System.out.println(container.getModel());
    }
}

