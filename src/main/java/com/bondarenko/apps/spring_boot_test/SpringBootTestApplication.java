package com.bondarenko.apps.spring_boot_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootTestApplication {
    public static void main(String[] args) {
        System.out.println("START!!!");
        SpringApplication.run(SpringBootTestApplication.class, args);
    }

}
