package com.bondarenko.apps.boot_js_app.repositories;

import com.bondarenko.apps.boot_js_app.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {
    boolean existsAuthorByEmail(String email);
}
