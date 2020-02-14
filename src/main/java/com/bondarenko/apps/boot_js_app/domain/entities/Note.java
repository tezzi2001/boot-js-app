package com.bondarenko.apps.boot_js_app.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "notes")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Note extends BaseEntity{
    @Column(name = "brief_description", length = 400, nullable = false)
    private String briefDescription;
    @Column(name = "full_description", length = 2500, nullable = false)
    private String fullDescription;
    @Column(name = "date", nullable = false)
    private Date date;
    @Column(name = "title", length = 80, nullable = false)
    private String title;
    @Column(name = "likeNums", nullable = false)
    private Integer likeNums;

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
