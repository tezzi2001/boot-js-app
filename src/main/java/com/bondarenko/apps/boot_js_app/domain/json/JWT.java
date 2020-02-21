package com.bondarenko.apps.boot_js_app.domain.json;

import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JWT extends Author {
    @JsonProperty("exp")
    private long exp;
    @JsonProperty("iat")
    private long iat;
    @JsonProperty("iss")
    private String iss;

    public Author toAuthor() {
        return new Author(login, "null", name, email, role, likedNotesId);
    }
}
