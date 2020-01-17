package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.domain.entities.Author;

public interface ISignService {
    boolean register(String login, String password, String name, String email);
    Author authorize(String login);
    Author authorize(String login, String password);
    boolean checkLogin(String login);
    boolean checkEmail(String email);
}
