package com.linqibin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author linqibin
 * @date 2023/9/10 12:39
 * @email 1214219989@qq.com
 */
@Configuration
public class WebConfig {
    
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

//    @Bean
//    public BeanNameUrlHandlerMapping beanNameUrlHandlerMapping() {
//        return new BeanNameUrlHandlerMapping();
//    }
//
//    @Bean
//    public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
//        return new SimpleControllerHandlerAdapter();
//    }

    /**
     * 自定义实现
     */
    @Component
    public static class MyBeanNameUrlHandlerMapping implements HandlerMapping {

        private Map<String, Controller> simpleControllerRegistry;

        @Autowired
        private ApplicationContext applicationContext;

        @Override
        public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
            Controller controller = simpleControllerRegistry.get(request.getRequestURI());
            if (controller != null) {
                return new HandlerExecutionChain(controller);
            }

            return null;
        }

        @PostConstruct
        public void init() {
            Map<String, Controller> beansOfType = applicationContext.getBeansOfType(Controller.class);
            simpleControllerRegistry = beansOfType.entrySet().stream().filter(entry -> entry.getKey().startsWith("/"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    @Component
    public static class MySimpleControllerAdapter implements HandlerAdapter {

        @Override
        public boolean supports(Object handler) {
            return handler instanceof Controller;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (handler instanceof Controller) {
                Controller controller = (Controller) handler;
                return controller.handleRequest(request, response);
            }
            return null;
        }

        @Override
        public long getLastModified(HttpServletRequest request, Object handler) {
            return -1;
        }
    }

    @Component("/c1")
    public static class Controller1 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("This is C1.");

            return null;
        }
    }

    @Component("c2")
    public static class Controller2 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("This is C2.");
            return null;
        }
    }

    @Bean(name = "/c3")
    public Controller controller3() {
        return (request, response) -> {
            response.getWriter().print("This is C3.");
            return null;
        };
    }

}