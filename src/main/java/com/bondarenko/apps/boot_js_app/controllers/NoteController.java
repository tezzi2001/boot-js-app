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
    public Note getNote(@PathVariable int id) {
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
        if (briefDescription == null || fullDescription == null || title == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        Note note = new Note(request.getParameter("briefDescription"), request.getParameter("fullDescription"), request.getParameter("title"));
        boolean isAdded = noteService.addNote(note, request.getParameter("login"));
        return new HashMap<String, String>() {{
            put("isAdded", String.valueOf(isAdded));
            put("id", note.getId().toString());
            put("brief_description", note.getBriefDescription());
            put("full_description", note.getFullDescription());
            put("title", note.getTitle());
            put("date", note.getDate().toString());
        }};
    }

    /**
     * Deletes the record in DB specified by id
     * @see INoteService#delete(int)
     * @param id An id that specifies the note
     * @return JSON object with field "isDeleted"
     */
    @DeleteMapping("/delete{id}")
    public Map deleteNote(@PathVariable int id) {
        boolean isDeleted = noteService.delete(id);
        return new HashMap<String, Boolean>() {{
            put("isDeleted", isDeleted);
        }};
    }
}
