package com.bondarenko.apps.boot_js_app.configurations;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@ComponentScan("com.bondarenko.apps.boot_js_app")
@EnableJpaRepositories("com.bondarenko.apps.boot_js_app.repositories")
@Profile("prod")
public class ProdDataConfig {
    @Value("${spring.datasource.driver-class-name}")
    private String DCN;

    @Bean
    @SneakyThrows
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        databaseUrl += "?useUnicode=true&amp;characterEncoding=UTF-8";
        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        dataSource.setUrl(url);
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        dataSource.setDriverClassName(DCN);
        return dataSource;
    }
}
