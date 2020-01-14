package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.Author;

public interface IJWTService {
    public String getToken(String username, String password);
    public Author getAuthorFromToken(String token) throws Exception;
}
