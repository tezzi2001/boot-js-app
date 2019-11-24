package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.BasicNote;

import java.util.List;

public interface INoteService {
    List<BasicNote> getNotes();
    BasicNote getNoteById(int id);
    boolean addNote(String login, String briefDescription, String fullDescription, String title);
    boolean delete(int id);
}
