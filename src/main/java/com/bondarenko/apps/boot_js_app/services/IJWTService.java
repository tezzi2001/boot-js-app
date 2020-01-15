package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.Author;

import java.util.Map;

public interface IJWTService {
    public Map<String, String> refreshTokens(String oldRefreshToken, String fingerprint);
    public String getToken(String login, String password);
    public Author getAuthorFromToken(String token) throws Exception;
}
