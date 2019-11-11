package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.services.IService;
import com.bondarenko.apps.boot_js_app.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Authorizes the user
     * @see Service#authorize(String, String)
     * @param request this is an input HTML form. It must contain fields "login" and "password"
     * @return JSON object with fields "name", "login" and "email" or JSON object with field "isAuthorized" depending on result of {@link Service#authorize(String, String)}
     */
    @PostMapping("/login")
    public Map authorize(HttpServletRequest request) {
        Author author = service.authorize(request.getParameter("login"), request.getParameter("password"));
        if (author != null) {
            return new HashMap<String, String>() {{
                put("isAuthorized", "true");
                put("name", author.getName());
                put("login", author.getLogin());
                put("email", author.getEmail());
            }};
        } else {
            return new HashMap<String, String>() {{
                put("isAuthorized", "false");
            }};
        }
    }

    /**
     * Registers the user
     * @see Service#register(String, String, String, String)
     * @param request this is an input HTML form. It must contain fields "login", "password", "name" and "email"
     * @return JSON object with field "isRegistered"
     */
    @PostMapping("/register")
    public Map register(HttpServletRequest request) {
        boolean isRegistered = service.register(request.getParameter("login"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        return new HashMap<String, Boolean>() {{
            put("isRegistered", isRegistered);
        }};
    }

    /**
     * Checks if user with current login exist in DB
     * @see Service#checkLogin(String)
     * @param request this is an input HTML form. It must contain field "login"
     * @return JSON object with field "isExist"
     */
    @PostMapping("/checkLogin")
    public Map checkLogin(HttpServletRequest request) {
        boolean isExist = service.checkLogin(request.getParameter("login"));
        return new HashMap<String, Boolean>() {{
            put("isExist", isExist);
        }};
    }

    /**
     * Checks if user with current email exist in DB
     * @see Service#checkEmail(String)
     * @param request this is an input HTML form. It must contain field "email"
     * @return JSON object with field "isExist"
     */
    @PostMapping("/checkEmail")
    public Map checkEmail(HttpServletRequest request) {
        boolean isExist = service.checkEmail(request.getParameter("email"));
        return new HashMap<String, Boolean>() {{
            put("isExist", isExist);
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

    /**
     * Creates new record in DB
     * @see Service#addNote(String, String, String, String)
     * @param request this is an input HTML form. It must contain fields "login", "name", "record" and "title"
     * @return JSON object with field "isAdded"
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map addNote(HttpServletRequest request) {
        boolean isAdded = service.addNote(request.getParameter("login"), request.getParameter("name"), request.getParameter("record"), request.getParameter("title"));
        return new HashMap<String, Boolean>() {{
            put("isAdded", isAdded);
        }};
    }

    @Autowired
    public void setService(IService service) {
        this.service = service;
    }
}
