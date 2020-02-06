package com.bondarenko.apps.boot_js_app.domain.aspects;

import com.bondarenko.apps.boot_js_app.repositories.JWTRepository;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@AllArgsConstructor
@Component
public class ServiceLogger {
    private Logger logger;
    private JWTRepository jwtRepository;

    @Pointcut(value = "execution(* com.bondarenko.apps.boot_js_app.services.JWTService.refreshTokens(String,..)) && args(fingerprint,..))", argNames = "fingerprint")
    private void callAtRefreshing(String fingerprint) {}

    @AfterReturning(value = "callAtRefreshing(fingerprint)", returning = "map", argNames = "fingerprint,map")
    public void log(String fingerprint, Map map) {
        if ("OK".equals(map.get("status"))) {
            logger.info("User " + jwtRepository.findJWTByRefreshToken(map.get("refreshToken").toString()).get().getLogin() + " with fingerprint " + fingerprint + " has refreshed JWT tokens");
        } else {
            logger.warn("User " + jwtRepository.findJWTByRefreshToken(map.get("refreshToken").toString()).get().getLogin() + " is not able to refresh tokens. Status: " + map.get("status"));
        }
    }
}
