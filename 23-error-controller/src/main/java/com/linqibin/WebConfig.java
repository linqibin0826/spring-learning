package com.linqibin;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linqibin
 * @date 2023/9/9 23:09
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

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return registry -> registry.addErrorPages(new ErrorPage("/error"));
    }

    @Bean
    public ErrorPageRegistrarBeanPostProcessor errorPageRegistrarBeanPostProcessor() {
        return new ErrorPageRegistrarBeanPostProcessor();
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return handlerAdapter;
    }

    @Bean
    public ViewResolver viewResolver() {
        return new BeanNameViewResolver();
    }

    /**
     * 当RequestMapping的produces是text/html时，需要返回一个名称为error的视图，这边手动创建一个假的视图
     * @return
     */
    @Bean
    public View error() {
        return new View() {
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                if (response.isCommitted()) {
                    Object path = model.get("path");
                    String message = "Cannot render error page for request [" + path + "]";
                    if (model.get("message") != null) {
                        message += " and exception [" + model.get("message") + "]";
                    }
                    message += " as the response has already been committed.";
                    message += " As a result, the response may have the wrong status code.";
                    return;
                }
                response.setContentType(new MediaType("text", "html", StandardCharsets.UTF_8).toString());
                StringBuilder builder = new StringBuilder();
                Object timestamp = model.get("timestamp");
                Object message = model.get("message");
                Object trace = model.get("trace");
                if (response.getContentType() == null) {
                    response.setContentType(getContentType());
                }
                builder.append("<html><body><h1>Whitelabel Error Page</h1>").append(
                                "<p>This application has no explicit mapping for /error, so you are seeing this as a fallback.</p>")
                        .append("<div id='created'>").append(timestamp).append("</div>")
                        .append("<div>There was an unexpected error (type=").append(htmlEscape(model.get("error")))
                        .append(", status=").append(htmlEscape(model.get("status"))).append(").</div>");
                if (message != null) {
                    builder.append("<div>").append(htmlEscape(message)).append("</div>");
                }
                if (trace != null) {
                    builder.append("<div style='white-space:pre-wrap;'>").append(htmlEscape(trace)).append("</div>");
                }
                builder.append("</body></html>");
                response.getWriter().append(builder.toString());
            }
        };

    }

    private String htmlEscape(Object input) {
        return (input != null) ? HtmlUtils.htmlEscape(input.toString()) : null;
    }

    @Bean
    public BasicErrorController basicErrorController() {
        return new BasicErrorController(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Controller
    public static class TestController {

        @RequestMapping("test")
        public void test() {
            int i = 1 / 0;
        }

        /**
         * SpringMVC的BasicErrorController就是做这个事情
         * @param request
         * @return
         */
//        @RequestMapping("/error")
//        @ResponseBody
        public Map<String, Object> error(HttpServletRequest request) {
            Throwable attribute = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            return new HashMap<String, Object>() {
                {
                    put("error", attribute.getMessage());
                }
            };
        }
    }
}

