package com.bondarenko.apps.boot_js_app.repositories;

import com.bondarenko.apps.boot_js_app.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
}
