package com.bondarenko.apps.boot_js_app.controllers;

import com.bondarenko.apps.boot_js_app.services.IJWTService;
import com.bondarenko.apps.boot_js_app.services.ISignService;
import lombok.AllArgsConstructor;
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
    public Map authenticate(String login, String fingerprint, String password) {
        return jwtService.getTokens(login, password, fingerprint);
    }

    /**
     * Registers the user
     * @see ISignService#register(String, String, String, String)
     * @return JSON object with field "isRegistered" or HTTP response with empty body and status 400
     */
    @PostMapping("/register")
    public Map register(String login, String fingerprint, String password, String name, String email) {
        boolean isRegistered = signService.register(login, password, name, email);
        Map<String, String> map = new HashMap<>();
        map.put("isRegistered", Boolean.toString(isRegistered));
        if (isRegistered) {
            map.putAll(jwtService.getTokens(login, password, fingerprint));
        }
        return map;
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

    @PostMapping("/getUserInfo")
    public String refreshInfo(String accessToken, String login) {
        return jwtService.getAccessTokenWithNewLikedNotesId(accessToken, signService.getAuthorById(login));
    }
}
