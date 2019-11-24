package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.BasicNote;
import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.repositories.AuthorRepository;
import com.bondarenko.apps.boot_js_app.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<BasicNote> getNotes() {
        return noteRepository.findAll().stream().map(BasicNote::new).collect(Collectors.toList());
    }

    @Override
    public BasicNote getNoteById(int id) {
        return noteRepository.findById(id).get().toBasicNote();
    }

    @Override
    public boolean addNote(String login, String briefDescription, String fullDescription, String title) {
        if (authorRepository.findById(login).isPresent()) {
            noteRepository.save(new Note(briefDescription, fullDescription, title));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        noteRepository.deleteById(id);
        return true;
    }
}
