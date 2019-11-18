package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.services.INoteService;
import com.bondarenko.apps.boot_js_app.services.ISignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a rest controller class
 * @author Bohdan Bondarenko
 * @version 1.0.1
 */
@RestController
public class RestApiController {
    private ISignService signService;
    private INoteService noteService;

    @Autowired
    public void setSignService(ISignService signService) {
        this.signService = signService;
    }

    @Autowired
    public void setNoteService(INoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Authorizes the user
     * @see ISignService#authorize(String, String)
     * @param request this is an input HTML form. It must contain fields "login" and "password"
     * @return JSON object with fields "name", "login", "email" and "role" or JSON object with field "isAuthorized"
     */
    @PostMapping("/login")
    public Map authorize(HttpServletRequest request) {
        Author author = signService.authorize(request.getParameter("login"), request.getParameter("password"));
        if (author != null) {
            return new HashMap<String, String>() {{
                put("isAuthorized", "true");
                put("name", author.getName());
                put("login", author.getLogin());
                put("email", author.getEmail());
                put("role", author.getRole());
            }};
        } else {
            return new HashMap<String, String>() {{
                put("isAuthorized", "false");
            }};
        }
    }

    /**
     * Registers the user
     * @see ISignService#register(String, String, String, String)
     * @param request this is an input HTML form. It must contain fields "login", "password", "name" and "email"
     * @return JSON object with field "isRegistered"
     */
    @PostMapping("/register")
    public Map register(HttpServletRequest request) {
        boolean isRegistered = signService.register(request.getParameter("login"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        return new HashMap<String, Boolean>() {{
            put("isRegistered", isRegistered);
        }};
    }

    /**
     * Checks if user with current login exist in DB
     * @see ISignService#checkLogin(String)
     * @param request this is an input HTML form. It must contain field "login"
     * @return JSON object with field "isExist"
     */
    @PostMapping("/checkLogin")
    public Map checkLogin(HttpServletRequest request) {
        boolean isExist = signService.checkLogin(request.getParameter("login"));
        return new HashMap<String, Boolean>() {{
            put("isExist", isExist);
        }};
    }

    /**
     * Checks if user with current email exist in DB
     * @see ISignService#checkEmail(String)
     * @param request this is an input HTML form. It must contain field "email"
     * @return JSON object with field "isExist"
     */
    @PostMapping("/checkEmail")
    public Map checkEmail(HttpServletRequest request) {
        boolean isExist = signService.checkEmail(request.getParameter("email"));
        return new HashMap<String, Boolean>() {{
            put("isExist", isExist);
        }};
    }

    /**
     * Returns all notes from DB
     * @see INoteService#getNotes()
     * @return array of JSON objects with fields "id", "date", "login" and "record"
     */
    @GetMapping("/getAll")
    public List<Note> getNotes() {
        return noteService.getNotes();
    }

    /**
     * Returns a note from DB specified by its id
     * @see INoteService#getNoteById(int)
     * @param id An id that specifies the note
     * @return JSON objects with fields "id", "date", "login" and "record"
     */
    @GetMapping("/get{id}")
    public Note getNote(@PathVariable int id) {
        return noteService.getNoteById(id);
    }

    /**
     * Creates new record in DB
     * @see INoteService#addNote(String, String, String, String)
     * @param request this is an input HTML form. It must contain fields "login", "name", "record" and "title"
     * @return JSON object with field "isAdded"
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map addNote(HttpServletRequest request) {
        boolean isAdded = noteService.addNote(request.getParameter("login"), request.getParameter("name"), request.getParameter("record"), request.getParameter("title"));
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
