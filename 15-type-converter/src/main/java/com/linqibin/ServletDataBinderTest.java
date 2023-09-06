package com.linqibin;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

/**
 * 测试web环境下的绑定
 * @Author linqibin
 * @Date 2023/9/6 22:22
 * @Email 1214219989@qq.com
 */
public class ServletDataBinderTest {

    public static void main(String[] args) {
        BeanWrapperImplTest.MyBean myBean = new BeanWrapperImplTest.MyBean();
        ServletRequestDataBinder binder = new ServletRequestDataBinder(myBean);
        // 模拟一个request对象
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", "linqibin");
        request.setParameter("password", "123");

        // 数据来源于request对象
        ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(request);
        binder.bind(pvs);
        System.out.println(myBean);
    }
}

