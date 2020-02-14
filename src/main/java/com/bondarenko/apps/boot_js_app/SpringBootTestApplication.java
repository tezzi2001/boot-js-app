package com.bondarenko.apps.boot_js_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringBootTestApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl == null) {
            System.err.println("Env var 'DATABASE_URL' is not defined!\n" +
                    "Its value should be equals to 'dev' on develop server");
            System.exit(0);
        }
        if ("DEV".equals(dbUrl)) {
            SpringApplication.run(SpringBootTestApplication.class, args).getEnvironment().setActiveProfiles("dev");
        } else {
            SpringApplication.run(SpringBootTestApplication.class, args).getEnvironment().setActiveProfiles("prod");
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootTestApplication.class);
    }
}
