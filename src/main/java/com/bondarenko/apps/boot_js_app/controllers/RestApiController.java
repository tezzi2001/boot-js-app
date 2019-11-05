package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.services.IService;
import com.bondarenko.apps.boot_js_app.services.Service;
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
    private IService service;

    /**
     * Method for testing. Will be deleted in future versions
     * @deprecated
     * @return string "Main page!(test)"
     */
    @Deprecated
    @GetMapping("/")
    public String getDefault() {
        return "Main page!(test)";
    }

    /**
     * Authorizes the user
     * @see Service#authorize(String, String)
     * @param request this is an input HTML form. It must contain fields "login" and "password"
     * @return JSON object with fields "name", "login" and "email"
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map authorize(HttpServletRequest request) {
        Author author = service.authorize(request.getParameter("login"), request.getParameter("password"));
        if (author != null) {
            return new HashMap<String, String>() {{
                put("name", author.getName());
                put("login", author.getLogin());
                put("email", author.getEmail());
            }};
        } else {
            return new HashMap<String, String>() {{
                put("name", null);
                put("login", null);
                put("email", null);
            }};
        }
    }

    /**
     * Registers the user
     * @see Service#register(String, String, String, String)
     * @param request this is an input HTML form. It must contain fields "login", "password", "name" and "email"
     * @return JSON object with field "isRegistered"
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map register(HttpServletRequest request) {
        boolean isRegistered = service.register(request.getParameter("login"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        return new HashMap<String, Boolean>() {{
            put("isRegistered", isRegistered);
        }};
    }

    /**
     * Returns all notes from DB
     * @see Service#getNotes()
     * @return array of JSON objects with fields "id", "date", "login" and "record"
     */
    @GetMapping("/getAll")
    public List<Note> getNotes() {
        return service.getNotes();
    }

    /**
     * Returns a note from DB specified by its id
     * @see Service#getNoteById(int)
     * @param id An id that specifies the note
     * @return JSON objects with fields "id", "date", "login" and "record"
     */
    @GetMapping("/get{id}")
    public Note getNote(@PathVariable int id) {
        return service.getNoteById(id);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public boolean addNote(HttpServletRequest request) {
        return service.addNote(request.getParameter("login"), request.getParameter("name"), request.getParameter("record"));
    }

    @Autowired
    public void setService(IService service) {
        this.service = service;
    }
}
