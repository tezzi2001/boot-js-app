package com.bondarenko.apps.boot_js_app.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @Column(name = "login", length = 20, nullable = false)
    @JsonProperty("login")
    protected String login;
    @Column(name = "password", length = 60, nullable = false)
    private String password;
    @Column(name = "name", length = 40, nullable = false)
    @JsonProperty("name")
    protected String name;
    @Column(name = "email", length = 40, nullable = false)
    @JsonProperty("email")
    protected String email;
    @Column(name = "role", length = 10, nullable = false)
    @JsonProperty("role")
    protected String role;
    @ElementCollection
    @JsonProperty("likedNotesId")
    protected List<Integer> likedNotesId;

    public void addLikedNoteId(int noteId) {
        if (likedNotesId == null) likedNotesId = new ArrayList<>();
        likedNotesId.add(noteId);
    }

    public void removeLikedNoteId(Integer noteId) {
        likedNotesId.remove(noteId);
    }

    public Integer[] getLikedNotesIdAsArray() {
        if (likedNotesId == null) likedNotesId = new ArrayList<>();
        return likedNotesId.toArray(new Integer[likedNotesId.size()]);
    }

    public static final String READER = "READER";
    public static final String ADMINISTRATOR = "ADMIN";
    public static final String MODERATOR = "MODERATOR";



}
