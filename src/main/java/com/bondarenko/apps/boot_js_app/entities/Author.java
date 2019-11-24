package com.bondarenko.apps.boot_js_app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @Column(name = "login", length = 20, nullable = false)
    private String login;
    @Column(name = "password", length = 20, nullable = false)
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

    public Author() {
        role = READER;
    }

    public Author(String login, String password, String name, String email) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.email = email;
        role = READER;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
