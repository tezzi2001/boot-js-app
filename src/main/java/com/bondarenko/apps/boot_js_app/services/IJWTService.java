package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.domain.entities.Author;

import java.io.IOException;
import java.util.Map;

public interface IJWTService {
    Map<String, String> getTokens(String login, String password, String fingerprint);
    Map<String, String> refreshTokens(String oldRefreshToken, String fingerprint);
    Author getAuthorFromToken(String token) throws IOException;
}
