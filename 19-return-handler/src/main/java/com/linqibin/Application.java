package com.linqibin;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import org.springframework.web.util.UrlPathHelper;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * @author linqibin
 */
public class Application {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);


        // 1.获取到默认的ReturnValueHandlers
        HandlerMethodReturnValueHandlerComposite composite = getValueHandlerComposite();
//        testModelAndView(context, composite);
//        testString(context, composite);
//        testModelAttributePresent(context, composite);
//        testHttpEntity(context, composite);
//        testHttpHeaders(context, composite);
        testResponseBody(context, composite);
    }

    /**
     * ResponseBody
     */
    private static void testResponseBody(AnnotationConfigApplicationContext context,
                                        HandlerMethodReturnValueHandlerComposite composite) throws Exception {
        WebConfig.Controller1 controller = new WebConfig.Controller1();
        ServletInvocableHandlerMethod handlerMethod =
                new ServletInvocableHandlerMethod(controller, controller.getClass().getDeclaredMethod("test07"));
        // mav容器
        ModelAndViewContainer container = new ModelAndViewContainer();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest request = new ServletWebRequest(mockRequest, response);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(controller.test07(), handlerMethod.getReturnType(), container, request);
            if (container.isRequestHandled()) {
                System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
                return;
            }
            renderView(context, container, request);
        }
    }

    /**
     * HttpHeaders
     */
    private static void testHttpHeaders(AnnotationConfigApplicationContext context,
                                       HandlerMethodReturnValueHandlerComposite composite) throws Exception {
        WebConfig.Controller1 controller = new WebConfig.Controller1();
        ServletInvocableHandlerMethod handlerMethod =
                new ServletInvocableHandlerMethod(controller, controller.getClass().getDeclaredMethod("test06"));
        // mav容器
        ModelAndViewContainer container = new ModelAndViewContainer();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest request = new ServletWebRequest(mockRequest, response);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(controller.test06(), handlerMethod.getReturnType(), container, request);
            if (container.isRequestHandled()) {
                System.out.println(response.getHeader("Content-Type"));
                return;
            }
            renderView(context, container, request);
        }
    }

    /**
     * HttpEntity
     */
    private static void testHttpEntity(AnnotationConfigApplicationContext context,
                                                  HandlerMethodReturnValueHandlerComposite composite) throws Exception {
        WebConfig.Controller1 controller = new WebConfig.Controller1();
        ServletInvocableHandlerMethod handlerMethod =
                new ServletInvocableHandlerMethod(controller, controller.getClass().getDeclaredMethod("test05"));
        // mav容器
        ModelAndViewContainer container = new ModelAndViewContainer();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest request = new ServletWebRequest(mockRequest, response);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(controller.test05(), handlerMethod.getReturnType(), container, request);
            if (container.isRequestHandled()) {
                System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
                return;
            }
            renderView(context, container, request);
        }
    }


    /**
     * 不存再@ModelAttribute时返回一个对象
     */
    private static void testModelAttributeNotPresent(AnnotationConfigApplicationContext context,
                                                     HandlerMethodReturnValueHandlerComposite composite) throws Exception {
        WebConfig.Controller1 controller = new WebConfig.Controller1();
        ServletInvocableHandlerMethod handlerMethod =
                new ServletInvocableHandlerMethod(controller, controller.getClass().getDeclaredMethod("test03"));
        // mav容器
        ModelAndViewContainer container = new ModelAndViewContainer();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        // 如果没有找到view名称，将会使用路径
        mockRequest.setRequestURI("/test03");
        UrlPathHelper.defaultInstance.resolveAndCacheLookupPath(mockRequest);
        ServletWebRequest request = new ServletWebRequest(mockRequest, new MockHttpServletResponse());
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(controller.test03(), handlerMethod.getReturnType(), container, request);
            renderView(context, container, request);
        }
    }

    /**
     * 使用了@ModelAttribute
     */
    private static void testModelAttributePresent(AnnotationConfigApplicationContext context,
                                                  HandlerMethodReturnValueHandlerComposite composite) throws Exception {
        WebConfig.Controller1 controller = new WebConfig.Controller1();
        ServletInvocableHandlerMethod handlerMethod =
                new ServletInvocableHandlerMethod(controller, controller.getClass().getDeclaredMethod("test04"));
        // mav容器
        ModelAndViewContainer container = new ModelAndViewContainer();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        // 如果没有找到view名称，将会使用路径
        mockRequest.setRequestURI("/test04");
        UrlPathHelper.defaultInstance.resolveAndCacheLookupPath(mockRequest);
        ServletWebRequest request = new ServletWebRequest(mockRequest, new MockHttpServletResponse());
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(controller.test04(), handlerMethod.getReturnType(), container, request);
            renderView(context, container, request);
        }
    }

    /**
     * 仅返回视图名称
     *
     * @param context   Spring应用上下文
     * @param composite returnValueHandler的组合
     */
    private static void testString(AnnotationConfigApplicationContext context, HandlerMethodReturnValueHandlerComposite composite) throws Exception {
        WebConfig.Controller1 controller = new WebConfig.Controller1();
        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(controller, controller.getClass().getDeclaredMethod("test02"));
        // mav容器
        ModelAndViewContainer container = new ModelAndViewContainer();
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());

        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(controller.test02(), handlerMethod.getReturnType(), container, request);
            renderView(context, container, request);
        }
    }

    /**
     * 测试返回值是ModelAndView
     */
    private static void testModelAndView(AnnotationConfigApplicationContext context, HandlerMethodReturnValueHandlerComposite composite) throws Exception {
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

    /**
     * 获取返回值处理器
     *
     * @return
     */
    @SuppressWarnings("all")
    private static HandlerMethodReturnValueHandlerComposite getValueHandlerComposite() throws Exception {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();

        // 设置一下MessageConverter
        handlerAdapter.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Method valueHandlers = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDefaultReturnValueHandlers");
        valueHandlers.setAccessible(true);
        List<HandlerMethodReturnValueHandler> returnValueHandlers = (List<HandlerMethodReturnValueHandler>) valueHandlers.invoke(handlerAdapter);


        composite.addHandlers(returnValueHandlers);

        return composite;
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