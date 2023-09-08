package com.linqibin;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class Application {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);


        // 1.获取到默认的ReturnValueHandlers
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        Method valueHandlers = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDefaultReturnValueHandlers");
        valueHandlers.setAccessible(true);
        List<HandlerMethodReturnValueHandler> returnValueHandlers = (List<HandlerMethodReturnValueHandler>) valueHandlers.invoke(handlerAdapter);
        composite.addHandlers(returnValueHandlers);

        WebConfig.Controller1 controller = new WebConfig.Controller1();
        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(controller, controller.getClass().getDeclaredMethod("test01"));
        // mav容器
        ModelAndViewContainer container = new ModelAndViewContainer();
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());

        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(controller.test01(), handlerMethod.getReturnType(), container, request);
            renderView(context, container, request);
        }

    }


    @SuppressWarnings("all")
    private static void renderView(AnnotationConfigApplicationContext context, ModelAndViewContainer container,
                                   ServletWebRequest webRequest) throws Exception {
        FreeMarkerViewResolver resolver = context.getBean(FreeMarkerViewResolver.class);
        String viewName = container.getViewName() != null ? container.getViewName() : new DefaultRequestToViewNameTranslator().getViewName(webRequest.getRequest());
        // 每次渲染时, 会产生新的视图对象, 它并非被 Spring 所管理, 但确实借助了 Spring 容器来执行初始化
        View view = resolver.resolveViewName(viewName, Locale.getDefault());

        view.render(container.getModel(), webRequest.getRequest(), webRequest.getResponse());
        System.out.println(new String(((MockHttpServletResponse) webRequest.getResponse()).getContentAsByteArray(), StandardCharsets.UTF_8));
    }
}