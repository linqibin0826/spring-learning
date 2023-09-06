package com.linqibin;

import lombok.Data;
import org.springframework.beans.DirectFieldAccessor;

/**
 * 直接通过字段反射，进行转换后赋值
 * @Author linqibin
 * @Date 2023/9/6 22:11
 * @Email 1214219989@qq.com
 */
public class DirectFieldAccessorTest {

    public static void main(String[] args) {
        Role role = new Role();
        DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(role);
        fieldAccessor.setPropertyValue("roleId", "1");
        fieldAccessor.setPropertyValue("roleName", "admin");
        System.out.println(role);
    }

    @Data
    static class Role {
        private String roleId;

        private String roleName;
    }
}

