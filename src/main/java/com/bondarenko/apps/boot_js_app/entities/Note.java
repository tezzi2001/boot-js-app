package com.bondarenko.apps.boot_js_app.entities;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "brief_description", length = 1024, nullable = false)
    private String briefDescription;
    @Column(name = "full_description", length = 1024, nullable = false)
    private String fullDescription;
    @Column(name = "date", nullable = false)
    private Date date;
    @Column(name = "title", length = 40, nullable = false)
    private String title;

    public Note() {}

    public Note(String briefDescription, String fullDescription, String title) {
        this.briefDescription = briefDescription;
        this.fullDescription = fullDescription;
        this.title = title;
        this.date = new Date();
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void getFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getDate() { ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
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
