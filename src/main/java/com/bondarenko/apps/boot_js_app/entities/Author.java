package com.bondarenko.apps.boot_js_app.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
public class Author {
    @Id
    @Column(name = "login", length = 20, nullable = false)
    private String login;
    @Column(name = "password", length = 60, nullable = false)
    private String password;
    @Column(name = "name", length = 40, nullable = false)
    private String name;
    @Column(name = "email", length = 40, nullable = false)
    private String email;
    @Column(name = "role", length = 10, nullable = false)
    private String role;

    public static final String READER = "READER";
    public static final String ADMINISTRATOR = "ADMIN";
    public static final String MODERATOR = "MODERATOR";

    public Author(String login, String password, String name, String email) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.email = email;
        role = READER;
    }
}
