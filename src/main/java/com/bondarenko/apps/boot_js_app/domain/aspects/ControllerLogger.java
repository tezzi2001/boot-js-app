package com.bondarenko.apps.boot_js_app.domain.aspects;

import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.bondarenko.apps.boot_js_app.services.JWTService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@AllArgsConstructor
@Component
public class ControllerLogger {
    private Logger logger;
    private JWTService jwtService;

    @Pointcut(value = "execution(* com.bondarenko.apps.boot_js_app.controllers.*.*(..)) && args(token,..)", argNames = "token")
    private void callAtAuthenticatedUser(String token) {}
    @Pointcut(value = "execution(* com.bondarenko.apps.boot_js_app.controllers.NoteController.deleteNote()) && args(id, token)", argNames = "id,token")
    private void callAtNoteDeletion(Integer id, String token) {}
    @Pointcut(value = "execution(* com.bondarenko.apps.boot_js_app.controllers.NoteController.addNote()) && args(token,..)", argNames = "token")
    private void callAtNoteAddition(String token) {}
    @Pointcut(value = "execution(* com.bondarenko.apps.boot_js_app.controllers.SignController.*.*(..)) && args(login, fingerprint,..)", argNames = "login,fingerprint")
    private void callAtAuthActions(String login, String fingerprint) {}

    @AfterThrowing(pointcut = "callAtAuthenticatedUser(token)", throwing = "e", argNames = "jp,e,token")
    public void log(JoinPoint jp, Exception e, String token) {
        logger.warn("Caught exception in " + jp.getSignature() + ". Exception: " + e.getMessage() + ". Token :" + token);
    }

    @SneakyThrows
    @AfterReturning(value = "callAtNoteAddition(token)", returning = "map", argNames = "token,map")
    public void log(String token, Map map) {
        Author author = jwtService.getAuthorFromToken(token);
        logger.info("User " + author.getLogin() + " has added note with ID " + map.get("id"));
    }

    @SneakyThrows
    @AfterReturning(value = "callAtNoteDeletion(id, token)", returning = "map", argNames = "id,token,map")
    public void log(Integer id, String token, Map map) {
        if (Boolean.TRUE.equals(map.get("isDeleted"))) {
            Author author = jwtService.getAuthorFromToken(token);
            logger.info("User " + author.getLogin() + " has deleted note with ID " + id);
        }
    }

    @AfterReturning(value = "callAtAuthActions(login, fingerprint)", returning = "map", argNames = "login,fingerprint,map")
    public void log(String login, String fingerprint, Map map) {
        if ("OK".equals(map.get("status"))) logger.info("User " + login + " has entered in a system with fingerprint " + fingerprint);
    }
}
