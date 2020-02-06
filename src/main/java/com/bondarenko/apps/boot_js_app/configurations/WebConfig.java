package com.bondarenko.apps.boot_js_app.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://vuejs-news-app.herokuapp.com",
                                "http://vuejs-news-app.herokuapp.com",
                                "http://localhost:8080")
                .maxAge(3600);
    }

    @Bean
    Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
