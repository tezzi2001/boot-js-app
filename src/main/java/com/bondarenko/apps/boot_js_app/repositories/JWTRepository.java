package com.bondarenko.apps.boot_js_app.repositories;

import com.bondarenko.apps.boot_js_app.domain.entities.JWT;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface JWTRepository  extends JpaRepository<JWT, String> {
    Optional<JWT> findJWTByRefreshToken(String refreshToken);
    @Transactional
    void deleteJWTByRefreshToken(String refreshToken);
    boolean existsJWTByFingerprint(String fingerprint);
    @Transactional
    void deleteJWTByFingerprint(String fingerprint);
}
