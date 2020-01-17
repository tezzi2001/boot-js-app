package com.bondarenko.apps.boot_js_app.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
@Data
//@JsonPropertyOrder({"login", "name", "email", "role"})
public class Author {
    @Id
    @Column(name = "login", length = 20, nullable = false)
    @JsonProperty("login")
    private String login;
    @Column(name = "password", length = 60, nullable = false)
    private String password;
    @Column(name = "name", length = 40, nullable = false)
    @JsonProperty("name")
    private String name;
    @Column(name = "email", length = 40, nullable = false)
    @JsonProperty("email")
    private String email;
    @Column(name = "role", length = 10, nullable = false)
    @JsonProperty("role")
    private String role;
    @JsonProperty("exp")
    private String exp;
    @JsonProperty("iat")
    private String iat;


    public static final String READER = "READER";
    public static final String ADMINISTRATOR = "ADMIN";
    public static final String MODERATOR = "MODERATOR";

    public Author(String login, String password, String name, String email, String role) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Author() {
    }
}
