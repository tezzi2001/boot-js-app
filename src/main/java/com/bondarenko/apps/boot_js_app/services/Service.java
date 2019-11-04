package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.repository.AuthorRepository;
import com.bondarenko.apps.boot_js_app.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service implements IService {
    private AuthorRepository authorRepository;
    private NoteRepository noteRepository;

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Autowired
    public void setNoteRepository(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    @Override
    public boolean register(String login, String password) {
        if (authorRepository.existsById(login)) {
            return false;
        } else {
            Author author = new Author(login, password);
            authorRepository.save(author);
            return true;
        }
    }

    @Override
    public boolean authorize(String login, String password) {
        if (authorRepository.findById(login).isPresent()) {
            return password.equals(authorRepository.findById(login).get().getPassword());
        } else {
            return false;
        }
    }

    @Override
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Note getNoteById(int id) {
        return noteRepository.findById(id).get();
    }
}
