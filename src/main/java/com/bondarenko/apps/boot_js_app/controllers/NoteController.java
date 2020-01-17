package com.bondarenko.apps.boot_js_app.controllers;
import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.bondarenko.apps.boot_js_app.domain.entities.Note;
import com.bondarenko.apps.boot_js_app.services.IJWTService;
import com.bondarenko.apps.boot_js_app.services.INoteService;
import com.bondarenko.apps.boot_js_app.services.ISignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a rest controller class
 * This class delegates {@link INoteService}
 * @author Bohdan Bondarenko
 * @version 1.0.1
 */
@CrossOrigin(origins = {"https://vuejs-news-app.herokuapp.com", "http://vuejs-news-app.herokuapp.com", "http://localhost:8080"})
@RestController
public class NoteController {
    private INoteService noteService;
    private IJWTService JWTService;
    private ISignService signService;

    @Autowired
    public void setService(ISignService service) {
        this.signService = service;
    }

    @Autowired
    public void setService(INoteService service) {
        this.noteService = service;
    }

    @Autowired
    public void setService(IJWTService service) {
        this.JWTService = service;
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
     * @param request this is an input HTML form. It must contain fields "token", "briefDescription", "fullDescription" and "title"
     * @param response HTTP response of the servlet
     * @return JSON object with fields "id", "isAdded", "brief_description", "full_description", "title", and "date" or JSON object with field "isAdded" or HTTP response with empty body and status 400
     */
    @PostMapping("/add")
    public Map addNote(HttpServletRequest request, HttpServletResponse response) {
        String briefDescription = request.getParameter("briefDescription");
        String fullDescription = request.getParameter("fullDescription");
        String title = request.getParameter("title");
        String token = request.getParameter("token");
        Note resultNote;
        boolean loginExists;
        Author author;

        try {
            author = JWTService.getAuthorFromToken(token);
            loginExists = signService.checkLogin(author.getLogin());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        if (briefDescription == null || fullDescription == null || title == null || !loginExists) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        resultNote = noteService.addNote(new Note(briefDescription, fullDescription, new Date(), title), author.getLogin());
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
     * @param request this is an input HTML form. It must contain field "token"
     * @param response HTTP response of the servlet
     * @return JSON object with field "isDeleted" or HTTP response with empty body and status 400
     */
    @PostMapping("/delete{id}")
    public Map deleteNote(@PathVariable int id, HttpServletResponse response, HttpServletRequest request) {
        boolean loginExists;
        boolean isAdmin = false;

        try {
            Author author = JWTService.getAuthorFromToken(request.getParameter("token"));
            loginExists = signService.checkLogin(author.getLogin());
            if(author.getRole().equals(Author.ADMINISTRATOR)) isAdmin = true;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        if (!noteService.existsById(id) || !loginExists || !isAdmin) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        boolean isDeleted = noteService.deleteNote(id);
        return new HashMap<String, Boolean>() {{
            put("isDeleted", isDeleted);
        }};
    }
}
