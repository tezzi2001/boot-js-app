package com.bondarenko.apps.boot_js_app.entities;

import java.util.Date;

public class BasicNote {
    private String briefDescription;
    private String fullDescription;
    private Date date;
    private String title;

    public BasicNote(Note note) {
        briefDescription = note.getBriefDescription();
        fullDescription = note.getFullDescription();
        date = note.getDate();
        title = note.getTitle();
    }

    BasicNote(String briefDescription, String fullDescription, String title, Date date) {
        this.briefDescription = briefDescription;
        this.fullDescription = fullDescription;
        this.title = title;
        this.date = date;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
}

