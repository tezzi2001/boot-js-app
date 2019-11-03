package com.bondarenko.apps.boot_js_app.entities;

import javax.persistence.*;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @Column(name = "login", length = 20, nullable = false)
    private String login;
    @Column(name = "password", length = 20, nullable = false)
    private String password;

    public Author() {}

    public Author(String login, String password) {
        this.login = login;
        this.password = password;
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
}
