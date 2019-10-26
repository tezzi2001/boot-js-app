package com.bondarenko.apps.spring_boot_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringBootTestApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        System.out.println("START!!!");
        SpringApplication.run(SpringBootTestApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootTestApplication.class);
    }
}
