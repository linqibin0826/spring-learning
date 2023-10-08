package com.linqibin;

import com.google.common.collect.Lists;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();

        // docBase
        File docBase = Files.createTempDirectory("boot.").toFile();
        docBase.deleteOnExit();

        // 3.创建tomcat项目， 每个项目就是tomcat中的Context
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(Config.class);
        applicationContext.refresh();

        // 4.添加servlet
        context.addServletContainerInitializer((c, ctx) -> {
                    ctx.addServlet("myServlet", new MyServlet()).addMapping("/hello");
                    // 通过servletRegistryBean注册容器中所有的servlet
                    for (ServletRegistrationBean registrationBean : applicationContext.getBeansOfType(ServletRegistrationBean.class).values()) {
                        registrationBean.onStartup(ctx);
                    }
                },
                Collections.emptySet());

        // 5.启动tomcat
        tomcat.start();

        // 6.创建连接器，设置监听端口
        Connector connector = new Connector(new Http11Nio2Protocol());
        connector.setPort(8080);
        tomcat.setConnector(connector);

    }

}

class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("<h3>Hello</h3>");
    }
}

@Configuration
class Config {

    @Bean
    public DispatcherServlet dispatcherServlet(WebApplicationContext applicationContext) {
        return new DispatcherServlet(applicationContext);
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setMessageConverters(Lists.newArrayList(new MappingJackson2HttpMessageConverter()));
        return adapter;
    }

    @RestController
    static class Controller {

        @GetMapping("/hello2")
        public Map<String, String> hello2() {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("hello", "world");
            return resultMap;
        }
    }

}