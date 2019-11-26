package com.bondarenko.apps.boot_js_app.services;
import com.bondarenko.apps.boot_js_app.entities.Note;

import java.util.List;

public interface INoteService {
    List<Note> getNotes();
    Note getNoteById(int id);
    Note addNote(Note note, String login);
    boolean deleteNote(int id);
    boolean existsById(int id);
}
