package com.linqibin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Map;

/**
 * @Author linqibin
 * @Date 2023/9/8 23:16
 * @Email 1214219989@qq.com
 */
@Configuration
public class WebConfig {

    @Controller
    public static class Controller1 {

        public ModelAndView test01() {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("hello");
            Map<String, Object> model = modelAndView.getModel();
            model.put("name", "linqibin");

            return modelAndView;
        }

        public String test02() {
            return "test02";
        }

        @ModelAttribute
        public User test03() {
            return new User("linqibin", 20);
        }

        public User test04() {
            return new User("youMei", 20);
        }

        public HttpEntity<User> test05() {
            User linqibin = new User("linqibin", 20);
            return new ResponseEntity<>(linqibin, HttpStatus.OK);
        }

        public HttpHeaders test06() {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "text/plain");
            return headers;
        }

        @ResponseBody
        public User test07() {
            return new User("youMei", 18);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String name;
        private Integer age;
    }


    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setDefaultEncoding("utf-8");
        configurer.setTemplateLoaderPath("classpath:templates");
        return configurer;
    }

    @Bean // FreeMarkerView 在借助 Spring 初始化时，会要求 web 环境才会走 setConfiguration, 这里想办法去掉了 web 环境的约束
    public FreeMarkerViewResolver viewResolver(FreeMarkerConfigurer configurer) {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver() {
            @Override
            protected AbstractUrlBasedView instantiateView() {
                FreeMarkerView view = new FreeMarkerView() {
                    @Override
                    protected boolean isContextRequired() {
                        return false;
                    }
                };
                view.setConfiguration(configurer.getConfiguration());
                return view;
            }
        };
        resolver.setContentType("text/html;charset=utf-8");
        resolver.setPrefix("/");
        resolver.setSuffix(".ftl");
        resolver.setExposeSpringMacroHelpers(false);
        return resolver;
    }
}

