package com.bondarenko.apps.boot_js_app.entities;

import javax.persistence.*;
import java.util.Date;

public class BasicNote {
    @Column(name = "brief_description", length = 1024, nullable = false)
    protected String briefDescription;
    @Column(name = "full_description", length = 1024, nullable = false)
    protected String fullDescription;
    @Column(name = "date", nullable = false)
    protected Date date;
    @Column(name = "title", length = 40, nullable = false)
    protected String title;

    protected BasicNote() {}

    protected BasicNote(String briefDescription, String fullDescription, String title) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

