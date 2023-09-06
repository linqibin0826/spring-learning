package com.linqibin;

import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

/**
 * @Author linqibin
 * @Date 2023/9/6 21:59
 * @Email 1214219989@qq.com
 */
public class SimpleTypeConverterTest {

    public static void main(String[] args) {

        // 简单的类型转换器
        SimpleTypeConverter converter = new SimpleTypeConverter();
        Integer integer = converter.convertIfNecessary("19", int.class);
        // 字符串到时间
        Date date = converter.convertIfNecessary("2019/01/01", Date.class);

        System.out.println(integer);
        System.out.println(date);
    }
}

