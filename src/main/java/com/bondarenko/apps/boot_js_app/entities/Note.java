package com.bondarenko.apps.boot_js_app.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notes")
public class Note extends BasicNote {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "login", length = 20, nullable = false)
    private String login;
    @Column(name = "name", length = 40, nullable = false)
    private String name;

    public Note() {};

    public Note(String login, String briefDescription, String fullDescription, String name, String title) {
        super(briefDescription, fullDescription, title);
        this.login = login;
        this.name = name;
        this.date = new Date();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void getFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }
}
