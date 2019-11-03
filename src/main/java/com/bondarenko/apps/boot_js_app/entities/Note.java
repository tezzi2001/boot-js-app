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
    @Column(name = "record", length = 1024, nullable = false)
    private String record;
    @Column(name = "date", nullable = false)
    private Date date;

    public Note() {}

    public Note(String login, String record) {
        this.record = record;
        this.login = login;
        this.date = new Date();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
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
}
