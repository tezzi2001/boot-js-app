package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.entities.BasicNote;
import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.services.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
     * @return array of JSON objects with fields "briefDescription", "fullDescription", "date" and "title"
     */
    @GetMapping("/getAll")
    public List<BasicNote> getNotes() {
        return noteService.getNotes();
    }

    /**
     * Returns a note from DB specified by its id
     * @see INoteService#getNoteById(int)
     * @param id An id that specifies the note
     * @return JSON object with fields "briefDescription", "fullDescription", "date" and "title"
     */
    @GetMapping("/get{id}")
    public BasicNote getNote(@PathVariable int id) {
        return noteService.getNoteById(id);
    }

    /**
     * Creates new record in DB
     * @see INoteService#addNote(String, String, String, String, String)
     * @param request this is an input HTML form. It must contain fields "login", "name", "record" and "title"
     * @return JSON object with field "isAdded"
     */
    @PostMapping("/add")
    public Map addNote(HttpServletRequest request) {
        boolean isAdded = noteService.addNote(request.getParameter("login"), request.getParameter("briefDescription"), request.getParameter("fullDescription"), request.getParameter("name"), request.getParameter("title"));
        return new HashMap<String, Boolean>() {{
            put("isAdded", isAdded);
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
