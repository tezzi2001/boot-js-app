package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.services.ISignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a rest controller class
 * This class delegates {@link ISignService}
 * @author Bohdan Bondarenko
 * @version 1.0.1
 */
@RestController
public class SignController {
    private ISignService signService;

    @Autowired
    public void setSignService(ISignService signService) {
        this.signService = signService;
    }

    /**
     * Authorizes the user
     * @see ISignService#authorize(String, String)
     * @param request this is an input HTML form. It must contain fields "login" and "password"
     * @return JSON object with fields "name", "login", "email" and "role" or JSON object with field "isAuthorized"
     */
    @PostMapping("/login")
    public Map authorize(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (login == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
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
    public Map register(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        if (login == null || password == null || name == null || email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
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
    public Map checkLogin(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        if (login == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
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
}
