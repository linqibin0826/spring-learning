package com.linqibin;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 获取参数化类型的两种方式
 * @Author linqibin
 * @Date 2023/9/6 23:41
 * @Email 1214219989@qq.com
 */
public class TypeParamTest {

    public static void main(String[] args) {
        // 1. JDK方式, Type是Class的父接口
        Type type = UserDao.class.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
            System.out.println(actualTypeArgument);
        }

        // 2.Spring
        Class<?> argument = GenericTypeResolver.resolveTypeArgument(UserDao.class, BaseDao.class);
        System.out.println(argument);
    }

    static class UserDao extends BaseDao<User> {

    }

    static class BaseDao<T> {

    }

    static class User {

    }
}

