package com.linqibin;

import lombok.Data;
import org.springframework.format.Formatter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Author linqibin
 * @Date 2023/9/6 22:49
 * @Email 1214219989@qq.com
 */
public class ServletDataBinderFactoryTest {

    public static void main(String[] args) throws Exception {
        User target = new User();

        // ①：用@InitBinder 转换，扫描拓展方法，即底层是基于PropertyEditor的拓展，表面上看上去是Formatter
        InvocableHandlerMethod initBinder = new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("initBinder", WebDataBinder.class));

        // ②：用ConversionService转换,  同时指定的时候会优先使用@InitBinder中自定义的转换器。
        FormattingConversionService service = new FormattingConversionService();
        service.addFormatter(new MyDateFormatter("我是用ConversionService转换的"));
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);

        // ③：使用默认的ConversionService转换， 要设置时间格式，使用@DateTimeFormat，内部会解析该注解. SpringBoot用的ApplicationConversionService
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        initializer.setConversionService(conversionService);

        // 使用工厂方法创建dataBinder(需要自己添加拓展)
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Lists.newArrayList(initBinder), initializer);

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthDay", "2019|06|06");
        request.setParameter("address.name", "上海");
        WebDataBinder binder = factory.createBinder(new ServletWebRequest(request), target, "user");
        // 把request转换成对应的PVS
        ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(request);
        binder.bind(pvs);
        System.out.println(target);

    }

    static class MyController {
        @InitBinder
        public void initBinder(WebDataBinder binder) {
            binder.addCustomFormatter(new MyDateFormatter("我是用@InitBinder转换的~"));
        }
    }

    @Data
    static class MyDateFormatter implements Formatter<Date> {

        private String desc;

        public MyDateFormatter(String desc) {
            this.desc = desc;
        }

        private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy|MM|dd");

        @Override
        public Date parse(String text, Locale locale) throws ParseException {
            System.out.println(desc);
            return DATE_FORMAT.parse(text);
        }

        @Override
        public String print(Date object, Locale locale) {
            System.out.println(desc);
            return DATE_FORMAT.format(object);
        }
    }

    @Data
    static class User {
        @DateTimeFormat(pattern = "yyyy|MM|dd")
        private Date birthDay;

        private Address address;
    }

    @Data
    static class Address {
        private String name;
    }
}

