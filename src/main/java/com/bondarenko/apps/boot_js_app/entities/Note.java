package com.bondarenko.apps.boot_js_app.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
public class Note {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "brief_description", length = 400, nullable = false)
    private String briefDescription;
    @Column(name = "full_description", length = 2500, nullable = false)
    private String fullDescription;
    @Column(name = "date", nullable = false)
    private Date date;
    @Column(name = "title", length = 80, nullable = false)
    private String title;

    public Note(String briefDescription, String fullDescription, String title) {
        this.briefDescription = briefDescription;
        this.fullDescription = fullDescription;
        this.title = title;
        this.date = new Date();
    }

    public String getDate() { ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
