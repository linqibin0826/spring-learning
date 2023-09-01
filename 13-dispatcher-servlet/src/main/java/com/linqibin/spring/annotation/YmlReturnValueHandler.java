package com.linqibin.spring.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;

/**
 * 自定义返回值处理器
 * @Author linqibin
 * @Date 2023/9/1 23:50
 * @Email 1214219989@qq.com
 */
public class YmlReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        YmlResponse ymlResponse = returnType.getMethodAnnotation(YmlResponse.class);
        return ymlResponse != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        // 1. 修改返回值
        String result = new Yaml().dump(returnValue);
        // 输出到响应体中
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("text/plain;charset=utf-8");
        response.getWriter().print(result);

        mavContainer.setRequestHandled(true);
    }
}

