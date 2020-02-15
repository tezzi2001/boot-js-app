package com.bondarenko.apps.boot_js_app.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Column(name = "liked_notes_id", columnDefinition = "varchar")
    @JsonProperty("likedNotesId")
    protected String likedNotesId;


    public List<Integer> getNotesId() {
        List<Integer> result = new ArrayList<>();
        String[] notesIdArray = likedNotesId.split(",");
        for (String noteId: notesIdArray) {
            result.add(Integer.parseInt(noteId));
        }
        return result;
    }

    public void addLikedNoteId(int noteId) {
        likedNotesId += "," + noteId;
    }

    public void removeLikedNoteId(int noteId) {
        String noteIdString = String.valueOf(noteId);
        String[] notesIdArray = likedNotesId.split(",");
        likedNotesId = "";
        Arrays.stream(notesIdArray).forEach((id) -> {
            if (!noteIdString.equals(id)) {
                likedNotesId += "," + id;
            }
        });
    }


    public static final String READER = "READER";
    public static final String ADMINISTRATOR = "ADMIN";
    public static final String MODERATOR = "MODERATOR";



}
