package com.bondarenko.apps.boot_js_app.repository;

import com.bondarenko.apps.boot_js_app.entities.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    @Override
    Page<Note> findAll(Pageable pageable);

    @Override
    Optional<Note> findById(Integer integer);
}
