package com.bondarenko.apps.boot_js_app.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.bondarenko.apps.boot_js_app")
@EnableJpaRepositories("com.bondarenko.apps.boot_js_app.repositories")
@PropertySource("classpath:application.properties")
public class DataConfig {
    @Value("${spring.datasource.url}")
    private String URL;
    @Value("${spring.datasource.driver-class-name}")
    private String DCN;
    @Value("${spring.datasource.username}")
    private String USERNAME;
    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Bean
    PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DCN);
        if (databaseUrl == null) {
            dataSource.setUrl(URL);
            dataSource.setPassword(PASSWORD);
            dataSource.setUsername(USERNAME);
        } else {
            URI dbUri;
            try {
                dbUri = new URI(databaseUrl);
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String url = "jdbc:postgresql://" + dbUri.getHost() + ':'
                        + dbUri.getPort() + dbUri.getPath();
                dataSource.setUrl(url);
                dataSource.setPassword(password);
                dataSource.setUsername(username);
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return dataSource;
    }
}