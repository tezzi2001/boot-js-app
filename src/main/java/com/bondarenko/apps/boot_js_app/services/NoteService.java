package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.bondarenko.apps.boot_js_app.domain.entities.Note;
import com.bondarenko.apps.boot_js_app.repositories.AuthorRepository;
import com.bondarenko.apps.boot_js_app.repositories.NoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NoteService implements INoteService {
    private AuthorRepository authorRepository;
    private NoteRepository noteRepository;

    @Override
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Note getNoteById(int id) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        return optionalNote.orElse(null);
    }

    @Override
    public Note addNote(Note note, String login) {
        if (authorRepository.findById(login).isPresent()) {
            Author author = authorRepository.getAuthorByLoginEquals(login);
            if (author.getRole().equals(Author.ADMINISTRATOR) || author.getRole().equals(Author.MODERATOR)) {
                return noteRepository.save(note);
            }
        }
        return null;
    }

    @Override
    public boolean deleteNote(int id) {
        noteRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(int id) {
        return noteRepository.existsById(id);
    }

    @Override
    public void incLikes(int id) {
        Note note = getNoteById(id);
        note.setLikesNum(note.getLikesNum()+1);
        deleteNote(id);
        noteRepository.save(note);
    }
}
