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
@CrossOrigin(origins = {"https://vuejs-news-app.herokuapp.com/"})
@RestController
public class NoteController {
    private INoteService service;

    @Autowired
    public void setService(INoteService service) {
        this.service = service;
    }

    /**
     * Returns all notes from DB
     * @see INoteService#getNotes()
     * @return array of JSON objects with fields "id", "briefDescription", "fullDescription", "date" and "title"
     */
    @GetMapping("/getAll")
    public List<Note> getNotes() {
        return service.getNotes();
    }

    /**
     * Returns a note from DB specified by its id
     * @see INoteService#getNoteById(int)
     * @param id An id that specifies the note
     * @param response HTTP response of the servlet
     * @return JSON object with fields "id", "briefDescription", "fullDescription", "date" and "title" or HTTP response with empty body and status 400
     */
    @GetMapping("/get{id}")
    public Note getNote(@PathVariable int id, HttpServletResponse response) {
        if (!service.existsById(id)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return service.getNoteById(id);
    }

    /**
     * Creates new record in DB
     * @see INoteService#addNote(Note, String)
     * @param request this is an input HTML form. It must contain fields "login", "briefDescription", "fullDescription" and "title"
     * @param response HTTP response of the servlet
     * @return JSON object with fields "id", "isAdded", "brief_description", "full_description", "title", and "date" or JSON object with field "isAdded" or HTTP response with empty body and status 400
     */
    @PostMapping("/add")
    public Map addNote(HttpServletRequest request, HttpServletResponse response) {
        String briefDescription = request.getParameter("briefDescription");
        String fullDescription = request.getParameter("fullDescription");
        String title = request.getParameter("title");
        String login = request.getParameter("login");
        Note resultNote;
        if (briefDescription == null || fullDescription == null || title == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        resultNote = service.addNote(new Note(briefDescription, fullDescription, title), login);
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
                put("date", resultNote.getDate());
                put("id", resultNote.getId().toString());
            }};
        }

    }

    /**
     * Deletes the record in DB specified by id
     * @see INoteService#deleteNote(int)
     * @param id An id that specifies the note
     * @param response HTTP response of the servlet
     * @return JSON object with field "isDeleted" or HTTP response with empty body and status 400
     */
    @DeleteMapping("/delete{id}")
    public Map deleteNote(@PathVariable int id, HttpServletResponse response) {
        if (!service.existsById(id)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        boolean isDeleted = service.deleteNote(id);
        return new HashMap<String, Boolean>() {{
            put("isDeleted", isDeleted);
        }};
    }
}
