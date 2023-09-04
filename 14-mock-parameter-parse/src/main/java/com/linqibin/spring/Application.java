package com.linqibin.spring;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;

/**
 * 模拟参数调用
 *
 * @Author linqibin
 * @Date 2023/9/3 21:13
 * @Email 1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 需要刷新容器， 否则无法获取变量${}
        context.refresh();

        // 将控制器中的方法，封装成HandlerMethod
        HandlerMethod testMethod = new HandlerMethod(new TestController(), TestController.class.getMethod("testMethod", String.class, Integer.class, String.class, String.class, MultipartFile.class, String.class, User.class, User.class, User.class));


        // 构造参数
        HttpServletRequest request = mockRequest();
        ServletWebRequest webRequest = new ServletWebRequest(request);
        ModelAndViewContainer container = new ModelAndViewContainer();

        // 一、第一个参数是beanFactory,用于解析${}这些变量， 第二个参数是如果参数没有注解，默认用该解析器处理
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        RequestParamMethodArgumentResolver resolver = new RequestParamMethodArgumentResolver(beanFactory, true);

        // 数据绑定， 类型转换工厂
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);

        // 二、用参数解析器的组合去逐个解析
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolvers(
                // 必须携带RequestParam参数
                new RequestParamMethodArgumentResolver(beanFactory, false),
                new RequestHeaderMethodArgumentResolver(beanFactory),
                new ServletModelAttributeMethodProcessor(false),
                new RequestResponseBodyMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter())),
                new ServletModelAttributeMethodProcessor(true),
                // 用来解析不带RequestParam注解的参数，加了这个  第三个参数才会被解析
                new RequestParamMethodArgumentResolver(beanFactory, true));

        for (MethodParameter methodParameter : testMethod.getMethodParameters()) {
            methodParameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
            // 解析参数
            if (composite.supportsParameter(methodParameter)) {
                Object o = composite.resolveArgument(methodParameter, container, webRequest, factory);

                Annotation[] parameterAnnotations = methodParameter.getParameterAnnotations();
                String str = "";
                if (parameterAnnotations.length > 0) {
                    str = parameterAnnotations[0].annotationType().getSimpleName() + " ";
                }
                System.out.println("[" + methodParameter.getParameterIndex() + "] " + str + methodParameter.getParameterName() + " " + o);
            }
        }


    }

    private static HttpServletRequest mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "hugh");
        request.setParameter("age", "20");
        request.setParameter("sex", "male");
        request.setContentType("application/json");
        request.addPart(new MockPart("attach", "profile", "hello".getBytes(StandardCharsets.UTF_8)));

        request.setParameter("username", "linqibin");
        request.setParameter("password", "123456");

        request.setContent("{\"username\":\"youmei\",\"password\":\"123\"}".getBytes(StandardCharsets.UTF_8));
        return new StandardServletMultipartResolver().resolveMultipart(request);
    }

    static class TestController {

        public void testMethod(@RequestParam("name") String name,
                               @RequestParam("age") Integer age,
                               String sex,
                               @RequestParam(name = "javaHome", defaultValue = "${JAVA_HOME}") String javaHome,
                               @RequestParam MultipartFile attach,
                               @RequestHeader(name = "Content-Type") String header,
                               @ModelAttribute User user,
                               User user1,
                               @RequestBody User user2) throws Exception {

        }
    }

    @Data
    static class User {
        private String username;

        private String password;
    }
}

