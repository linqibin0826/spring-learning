package com.linqibin.spring.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.linqibin.spring.component.Bean1;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Author linqibin
 * @Date 2023/8/24 22:39
 * @Email 1214219989@qq.com
 */
@Configuration
@ComponentScan(basePackages = {"com.linqibin.spring"})
public class BeanConfig {

    @Bean
    public Bean1 bean1() {
        return new Bean1();
    }

    @Bean(initMethod = "init")
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://123.60.92.39:13306/mogu_blog");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean;
    }
}

