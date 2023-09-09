package com.linqibin;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;

/**
 * 1. @ResponseBody是返回值处理器处理的，但具体的消息转换用的时MessageConverter，由处理器进行调用
 * 2. SpringMVC如何选择MediaType，  首先看@RequestBody注解里有没有指定（即指定Response的Content-Type）
 *    其次看request对象的Accept这个Header
 *    最好按MessageConverters添加的顺序，谁先就用谁。
 * @Author linqibin
 * @Date   2023/9/9 15:35
 * @Email  1214219989@qq.com
 */
public class Application {

    public static void main(String[] args) throws Exception {
        // test01 转成Json输出
        test01();
        // test02 转成xml
        test02();
        // test03 把输入转成对象
        test03();

        test04();

    }

    public static void test04() throws Exception {

        RequestResponseBodyMethodProcessor processor = new RequestResponseBodyMethodProcessor(
                Lists.newArrayList(
                        new MappingJackson2HttpMessageConverter(),
                        new MappingJackson2XmlHttpMessageConverter()
                )
        );

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("Accept", "application/xml");
//        response.setContentType("application/json");
        ServletWebRequest webRequest = new ServletWebRequest(request, response);


        processor.handleReturnValue(new User("linqibin", 20),
                new MethodParameter(Controller1.class.getMethod("test", User.class), -1),
                new ModelAndViewContainer(),
                webRequest
        );

        System.out.println(response.getContentAsString(StandardCharsets.UTF_8));
    }

    public static void test03() throws Exception {
        MockHttpInputMessage message =
                new MockHttpInputMessage("{\"name\":\"linqibin\",\"age\":20}".getBytes(StandardCharsets.UTF_8));

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (converter.canRead(User.class, APPLICATION_JSON)) {
            System.out.println(converter.read(User.class, message));
        }

    }

    public static void test02() throws Exception {
        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();

        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        if (converter.canWrite(User.class, APPLICATION_XML)) {

            User user = new User("linqibin", 20);
            converter.write(user, APPLICATION_XML, outputMessage);
            System.out.println(outputMessage.getBodyAsString(StandardCharsets.UTF_8));
        }
    }

    public static void test01() throws Exception {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        if (converter.canWrite(User.class, APPLICATION_JSON)) {

            User user = new User("linqibin", 20);
            converter.write(user, APPLICATION_JSON, outputMessage);
            System.out.println(outputMessage.getBodyAsString(StandardCharsets.UTF_8));
        }
    }

    public static class Controller1 {

        public void test(User user) {

        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String name;
        private Integer age;
    }
}