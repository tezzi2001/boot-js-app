package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.services.IJWTService;
import com.bondarenko.apps.boot_js_app.services.ISignService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a rest controller class
 * This class delegates {@link ISignService}
 * @author Bohdan Bondarenko
 * @version 1.0.1
 */
@AllArgsConstructor
@RestController
public class SignController {
    private ISignService signService;
    private IJWTService jwtService;

    /**
     * Authenticates the user
     * @see IJWTService#getTokens(String, String, String)
     * @return JSON object with fields "status", "accessToken" and "refreshToken" or HTTP response with empty body and status 400
     */
    @PostMapping("/login")
    public Map authenticate(String login, String password, String fingerprint) {
        return jwtService.getTokens(login, password, fingerprint);
    }

    /**
     * Registers the user
     * @see ISignService#register(String, String, String, String)
     * @return JSON object with field "isRegistered" or HTTP response with empty body and status 400
     */
    @PostMapping("/register")
    public Map register(String login, String password, String fingerprint, String name, String email) {
        return new HashMap<String, String>() {{
            put("isRegistered", String.valueOf(signService.register(login, password, name, email)));
            putAll(jwtService.getTokens(login, password, fingerprint));
        }};
    }

    /**
     * Checks if user with current login exist in DB
     * @see ISignService#checkLogin(String)
     * @return JSON object with field "isExist" or HTTP response with empty body and status 400
     */
    @PostMapping("/checkLogin")
    public Map checkLogin(String login) {
        return new HashMap<String, Boolean>() {{
            put("isExist", signService.checkLogin(login));
        }};
    }

    /**
     * Checks if user with current email exist in DB
     * @see ISignService#checkEmail(String)
     * @return JSON object with field "isExist" or HTTP response with empty body and status 400
     */
    @PostMapping("/checkEmail")
    public Map checkEmail(String email) {
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
