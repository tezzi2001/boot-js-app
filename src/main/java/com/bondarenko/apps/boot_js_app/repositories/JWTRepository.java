package com.bondarenko.apps.boot_js_app.repositories;

import com.bondarenko.apps.boot_js_app.entities.JWT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JWTRepository  extends JpaRepository<JWT, String> {
}
