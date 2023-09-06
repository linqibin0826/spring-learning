package com.linqibin;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

/**
 * @Author linqibin
 * @Date 2023/9/6 22:14
 * @Email 1214219989@qq.com
 */
public class DataBinderTest {

    public static void main(String[] args) {
        BeanWrapperImplTest.MyBean bean = new BeanWrapperImplTest.MyBean();
        DataBinder binder = new DataBinder(bean);

        // 开启该选项后，如果有set方法，就走set。如果没有，就直接通过字段赋值。
        binder.initDirectFieldAccess();

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("username", "linqibin");
        propertyValues.addPropertyValue("password", "123");

        binder.bind(propertyValues);
        System.out.println(bean);
    }
}

