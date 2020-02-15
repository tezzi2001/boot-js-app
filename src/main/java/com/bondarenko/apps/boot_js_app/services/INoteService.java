package com.bondarenko.apps.boot_js_app.services;
import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.bondarenko.apps.boot_js_app.domain.entities.Note;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface INoteService {
    List<Note> getNotes();
    Note getNoteById(int id);
    Note addNote(Note note, String login);
    boolean deleteNote(int id);
    boolean existsById(int id);
    @Transactional
    Map incLikes(int id, Author author, String token);
    @Transactional
    Map decLikes(int id, Author author, String token);
}
