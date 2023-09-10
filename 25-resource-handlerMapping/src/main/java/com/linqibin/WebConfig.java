package com.linqibin;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.resource.CachingResourceResolver;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Map;

/**
 * @author linqibin
 * @date 2023/9/10 14:09
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
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping(ApplicationContext applicationContext) {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        // 没有初始化方法进行扫描，因此需要手动注册
        Map<String, ResourceHttpRequestHandler> urlMaps = applicationContext.getBeansOfType(ResourceHttpRequestHandler.class);
        mapping.setUrlMap(urlMaps);
        return mapping;
    }

    @Bean
    public HttpRequestHandlerAdapter httpRequestHandlerAdapter() {
        return new HttpRequestHandlerAdapter();
    }


    @Bean("/**")
    public ResourceHttpRequestHandler handler1() {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(Lists.newArrayList(new ClassPathResource("static/")));
        // 责任链模式，会生成一条解析器链
        handler.setResourceResolvers(Lists.newArrayList(
                // 缓存
                new CachingResourceResolver(new ConcurrentMapCache("cache")),
                // 压缩
                new EncodedResourceResolver(),
                // 去磁盘找
                new PathResourceResolver()
        ));
        return handler;
    }

    @Bean("/images/**")
    public ResourceHttpRequestHandler handler2() {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(Lists.newArrayList(new ClassPathResource("images/")));
        return handler;
    }
}

