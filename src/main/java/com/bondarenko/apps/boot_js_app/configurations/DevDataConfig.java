package com.bondarenko.apps.boot_js_app.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.bondarenko.apps.boot_js_app")
@EnableJpaRepositories("com.bondarenko.apps.boot_js_app.repositories")
@PropertySource("classpath:application.properties")
@Profile("dev")
public class DevDataConfig {
    @Value("${spring.datasource.url}")
    private String URL;
    @Value("${spring.datasource.driver-class-name}")
    private String DCN;
    @Value("${spring.datasource.username}")
    private String USERNAME;
    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(DCN);
        dataSource.setUrl(URL);
        dataSource.setPassword(PASSWORD);
        dataSource.setUsername(USERNAME);
        return dataSource;
    }
}