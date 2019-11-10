package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.entities.Note;

import java.util.List;

public interface IService {
    boolean register(String login, String password, String name, String email);
    boolean authorize(String login, String password);
    List<Note> getNotes();
    Note getNoteById(int id);
    boolean addNote(String login, String name, String record, String title);
}
