package com.bondarenko.apps.boot_js_app.controllers;
import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.services.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a rest controller class
 * This class delegates {@link INoteService}
 * @author Bohdan Bondarenko
 * @version 1.0.1
 */
@RestController
public class NoteController {
    private INoteService noteService;

    @Autowired
    public void setNoteService(INoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Returns all notes from DB
     * @see INoteService#getNotes()
     * @return array of JSON objects with fields "id", "briefDescription", "fullDescription", "date" and "title"
     */
    @GetMapping("/getAll")
    public List<Note> getNotes() {
        return noteService.getNotes();
    }

    /**
     * Returns a note from DB specified by its id
     * @see INoteService#getNoteById(int)
     * @param id An id that specifies the note
     * @return JSON object with fields "id", "briefDescription", "fullDescription", "date" and "title"
     */
    @GetMapping("/get{id}")
    public Note getNote(@PathVariable int id, HttpServletResponse response) {
        if (!noteService.existsById(id)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return noteService.getNoteById(id);
    }

    /**
     * Creates new record in DB
     * @see INoteService#addNote(Note, String)
     * @param request this is an input HTML form. It must contain fields "login", "briefDescription", "fullDescription" and "title"
     * @return JSON object with fields "id", "isAdded", "brief_description", "full_description", "title", and "date"
     */
    @PostMapping("/add")
    public Map addNote(HttpServletRequest request, HttpServletResponse response) {
        String briefDescription = request.getParameter("briefDescription");
        String fullDescription = request.getParameter("fullDescription");
        String title = request.getParameter("title");
        Note note;
        Note resultNote;
        if (briefDescription == null || fullDescription == null || title == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        note = new Note(briefDescription, fullDescription, title);
        resultNote = noteService.addNote(note, request.getParameter("login"));
        if (resultNote == null) {
            return new HashMap<String, String>() {{
                put("isAdded", "false");
            }};
        } else {
            return new HashMap<String, String>() {{
                put("isAdded", "true");
                put("brief_description", resultNote.getBriefDescription());
                put("full_description", resultNote.getFullDescription());
                put("title", resultNote.getTitle());
                put("date", resultNote.getDate().toString());
                put("id", resultNote.getId().toString());
            }};
        }

    }

    /**
     * Deletes the record in DB specified by id
     * @see INoteService#delete(int)
     * @param id An id that specifies the note
     * @return JSON object with field "isDeleted"
     */
    @DeleteMapping("/delete{id}")
    public Map deleteNote(@PathVariable int id, HttpServletResponse response) {
        if (!noteService.existsById(id)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        boolean isDeleted = noteService.delete(id);
        return new HashMap<String, Boolean>() {{
            put("isDeleted", isDeleted);
        }};
    }
}
