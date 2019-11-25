package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.repositories.AuthorRepository;
import com.bondarenko.apps.boot_js_app.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService implements INoteService {
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
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Note getNoteById(int id) {
        return noteRepository.findById(id).get();
    }

    @Override
    public boolean addNote(Note note, String login) {
        if (authorRepository.findById(login).isPresent()) {
            Author author = authorRepository.getAuthorByLoginEquals(login);
            if (author.getRole().equals(Author.ADMINISTRATOR) || author.getRole().equals(Author.MODERATOR)) {
                noteRepository.save(note);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        noteRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(int id) {
        return noteRepository.existsById(id);
    }
}
