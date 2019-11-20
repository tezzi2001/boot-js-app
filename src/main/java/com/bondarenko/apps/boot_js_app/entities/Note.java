package com.bondarenko.apps.boot_js_app.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "login", length = 20, nullable = false)
    private String login;
    @Column(name = "name", length = 40, nullable = false)
    private String name;
    @Column(name = "brief_description", length = 1024, nullable = false)
    private String briefDescription;
    @Column(name = "full_description", length = 1024, nullable = false)
    private String fullDescription;
    @Column(name = "date", nullable = false)
    private Date date;
    @Column(name = "title", length = 40, nullable = false)
    private String title;

    public Note() {}

    public Note(String login, String briefDescription, String fullDescription, String name, String title) {
        this.login = login;
        this.briefDescription = briefDescription;
        this.fullDescription = fullDescription;
        this.name = name;
        this.title = title;
        this.date = new Date();
    }

    public BasicNote toBasicNote() {
        return new BasicNote(getBriefDescription(), getFullDescription(), getTitle(), getDate());
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
