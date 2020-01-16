package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.services.IJWTService;
import com.bondarenko.apps.boot_js_app.services.ISignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = {"https://vuejs-news-app.herokuapp.com", "http://vuejs-news-app.herokuapp.com", "http://localhost:8080"})
@RestController
public class SignController {
    private ISignService service;
    private IJWTService JWTService;

    @Autowired
    public void setService(ISignService service) {
        this.service = service;
    }

    @Autowired
    public void setService(IJWTService service) {
        this.JWTService = service;
    }

    /**
     * Authorizes the user
     * @see ISignService#authorize(String, String)
     * @param request this is an input HTML form. It must contain fields "login", "password" and "fingerprint"
     * @param response HTTP response of the servlet
     * @return JSON object with fields "status", "accessToken" and "refreshToken" or HTTP response with empty body and status 400
     */
    @PostMapping("/login")
    public Map authorize(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String fingerprint = request.getParameter("fingerprint");
        if (login == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return JWTService.getTokens(login, password, fingerprint);
    }

    /**
     * Registers the user
     * @see ISignService#register(String, String, String, String)
     * @param request this is an input HTML form. It must contain fields "login", "password", "name" and "email"
     * @param response HTTP response of the servlet
     * @return JSON object with field "isRegistered" or HTTP response with empty body and status 400
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
        boolean isRegistered = service.register(login, password, name, email);
        return new HashMap<String, Boolean>() {{
            put("isRegistered", isRegistered);
        }};
    }

    /**
     * Checks if user with current login exist in DB
     * @see ISignService#checkLogin(String)
     * @param request this is an input HTML form. It must contain field "login"
     * @param response HTTP response of the servlet
     * @return JSON object with field "isExist" or HTTP response with empty body and status 400
     */
    @PostMapping("/checkLogin")
    public Map checkLogin(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        if (login == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        boolean isExist = service.checkLogin(login);
        return new HashMap<String, Boolean>() {{
            put("isExist", isExist);
        }};
    }

    /**
     * Checks if user with current email exist in DB
     * @see ISignService#checkEmail(String)
     * @param request this is an input HTML form. It must contain field "email"
     * @param response HTTP response of the servlet
     * @return JSON object with field "isExist" or HTTP response with empty body and status 400
     */
    @PostMapping("/checkEmail")
    public Map checkEmail(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        if (email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        boolean isExist = service.checkEmail(email);
        return new HashMap<String, Boolean>() {{
            put("isExist", isExist);
        }};
    }

    /**
     * Refreshes refreshToken
     * @see IJWTService#refreshTokens(String, String)
     * @param request this is an input HTML form. It must contain fields "refreshToken" and "fingerprint"
     * @return JSON object with fields "status", "accessToken" and "refreshToken"
     */
    @PostMapping("/refresh")
    public Map refresh(HttpServletRequest request) {
        String refreshToken = request.getParameter("refreshToken");
        String fingerprint = request.getParameter("fingerprint");

        return JWTService.refreshTokens(refreshToken, fingerprint);
    }
}
