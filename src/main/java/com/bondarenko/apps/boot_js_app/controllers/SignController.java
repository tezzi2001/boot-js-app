package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.services.IJWTService;
import com.bondarenko.apps.boot_js_app.services.ISignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private ISignService signService;
    private IJWTService jwtService;

    @Autowired
    public void setService(ISignService service) {
        this.signService = service;
    }

    @Autowired
    public void setService(IJWTService service) {
        this.jwtService = service;
    }

    /**
     * Authorizes the user
     * @see ISignService#authorize(String, String)
     * @param response HTTP response of the servlet
     * @return JSON object with fields "status", "accessToken" and "refreshToken" or HTTP response with empty body and status 400
     */
    @PostMapping("/login")
    public Map authorize(String login, String password, String fingerprint, HttpServletResponse response) {
        if (login == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return jwtService.getTokens(login, password, fingerprint);
    }

    /**
     * Registers the user
     * @see ISignService#register(String, String, String, String)
     * @param response HTTP response of the servlet
     * @return JSON object with field "isRegistered" or HTTP response with empty body and status 400
     */
    @PostMapping("/register")
    public Map register(String login, String password, String name, String email, HttpServletResponse response) {
        if (login == null || password == null || name == null || email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return new HashMap<String, Boolean>() {{
            put("isRegistered", signService.register(login, password, name, email));
        }};
    }

    /**
     * Checks if user with current login exist in DB
     * @see ISignService#checkLogin(String)
     * @param response HTTP response of the servlet
     * @return JSON object with field "isExist" or HTTP response with empty body and status 400
     */
    @PostMapping("/checkLogin")
    public Map checkLogin(String login, HttpServletResponse response) {
        if (login == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return new HashMap<String, Boolean>() {{
            put("isExist", signService.checkLogin(login));
        }};
    }

    /**
     * Checks if user with current email exist in DB
     * @see ISignService#checkEmail(String)
     * @param response HTTP response of the servlet
     * @return JSON object with field "isExist" or HTTP response with empty body and status 400
     */
    @PostMapping("/checkEmail")
    public Map checkEmail(String email, HttpServletResponse response) {
        if (email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return new HashMap<String, Boolean>() {{
            put("isExist", signService.checkEmail(email));
        }};
    }

    /**
     * Refreshes refreshToken
     * @see IJWTService#refreshTokens(String, String)
     * @return JSON object with fields "status", "accessToken" and "refreshToken"
     */
    @PostMapping("/refresh")
    public Map refresh(String refreshToken, String fingerprint) {
        return jwtService.refreshTokens(refreshToken, fingerprint);
    }
}
