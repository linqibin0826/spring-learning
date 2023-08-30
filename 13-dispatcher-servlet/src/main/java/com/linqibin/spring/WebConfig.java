package com.linqibin.spring;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @Author linqibin
 * @Date 2023/8/30 22:47
 * @Email 1214219989@qq.com
 */
@Configuration
@ComponentScan
// 指定去哪里读取属性
@PropertySource("classpath:/application.properties")
@EnableConfigurationProperties(value = {WebMvcProperties.class, ServerProperties.class})
public class WebConfig {

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(serverProperties.getPort());
        return factory;
    }

    /**
     * 初始化时机：①.如果没有配置loadOnStartup，则会在首次访问时初始化。
     *
     * @return
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(
            DispatcherServlet dispatcherServlet, WebMvcProperties webMvcProperties) {
        DispatcherServletRegistrationBean bean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        bean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
        return bean;
    }

}

