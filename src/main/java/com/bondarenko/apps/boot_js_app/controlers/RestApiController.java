package com.bondarenko.apps.boot_js_app.controlers;

import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.services.IService;
import com.bondarenko.apps.boot_js_app.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
     * @return JSON object with field "isAuthorized"
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List authorize(HttpServletRequest request) {
        boolean isAuthorized = service.authorize(request.getParameter("login"), request.getParameter("password"));
        if (isAuthorized) {
            return new ArrayList<HashMap>() {{
                add(new HashMap<String, Boolean>() {{
                        put("isAuthorized", isAuthorized);
                    }});
                add(new HashMap<String, String>() {{
                    put("login", request.getParameter("login"));
                }});
            }};
        } else {
            return new ArrayList<HashMap>() {{
                add(new HashMap<String, Boolean>() {{
                    put("isAuthorized", isAuthorized);
                }});
                add(new HashMap<String, String>() {{
                    put("login", null);
                }});
            }};
        }
    }

    /**
     * Registers the user
     * @see Service#register(String, String)
     * @param request this is an input HTML form. It must contain fields "login" and "password"
     * @return JSON object with field "isRegistered"
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map register(HttpServletRequest request) {
        boolean isRegistered = service.register(request.getParameter("login"), request.getParameter("password"));
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

    @Autowired
    public void setService(IService service) {
        this.service = service;
    }
}
