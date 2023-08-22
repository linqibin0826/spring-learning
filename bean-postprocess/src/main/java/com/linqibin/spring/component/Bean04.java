package com.linqibin.spring.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 测试ConfigurationProperties
 *
 * @Author linqibin
 * @Date 2023/8/22 23:14
 * @Email 1214219989@qq.com
 */
@Data
@ConfigurationProperties(prefix = "java")
public class Bean04 {

    private String home;

    private String version;
}

