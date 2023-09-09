package com.linqibin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author linqibin
 * @date 2023/9/9 17:58
 * @email 1214219989@qq.com
 */
@Configuration
public class WebConfig {

    /**
     * RequestBodyAdvice可以用来对数据全局做一些前置处理
     * @Author linqibin
     * @Date   2023/9/9 20:21
     * @Email  1214219989@qq.com
     */
    @ControllerAdvice
    public static class Advice1 implements RequestBodyAdvice,ResponseBodyAdvice<Object> {

        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return returnType.getMethodAnnotation(ResponseBody.class) != null
                    || AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
            return Result.OK(body);
        }

        @Override
        public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

            return methodParameter.getParameterAnnotation(RequestBody.class) != null;
        }

        @Override
        public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
            return inputMessage;
        }

        @Override
        public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
            return body;
        }

        @Override
        public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
            return body;
        }
    }

    @Controller
    public static class Controller1 {

        @ResponseBody
        public User user(@RequestBody String name) {
            return new User(name);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String username;
    }

    @Data
    @Builder
    public static class Result {


        private Integer code;

        private Object data;

        public static Result OK(Object data) {
            return new Result(200, data);
        }
    }

}

