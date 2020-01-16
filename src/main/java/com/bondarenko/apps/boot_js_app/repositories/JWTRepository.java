package com.bondarenko.apps.boot_js_app.repositories;

import com.bondarenko.apps.boot_js_app.entities.JWT;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface JWTRepository  extends JpaRepository<JWT, String> {
    @Transactional
    JWT deleteJWTByRefreshToken(String refreshToken);
    boolean existsJWTByFingerprint(String fingerprint);
    @Transactional
    void deleteJWTByFingerprint(String fingerprint);
}