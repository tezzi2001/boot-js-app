package com.bondarenko.apps.boot_js_app.controlers;

import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.services.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RestApiController {
    private IService service;

    @GetMapping("/")
    public String getDefault() {
        return "Main page!";
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public boolean authorize(HttpServletRequest request) {
        return service.authorize(request.getParameter("login"), request.getParameter("password"));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public boolean register(HttpServletRequest request) {
        return service.register(request.getParameter("login"), request.getParameter("password"));
    }

    @GetMapping("/getAll")
    public List<Note> getNotes() {
        return service.getNotes();
    }

    @GetMapping("/get{id}")
    public Note getNote(@PathVariable int id) {
        return service.getNoteById(id);
    }

    @Autowired
    public void setService(IService service) {
        this.service = service;
    }
}
