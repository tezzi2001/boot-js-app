package com.bondarenko.apps.boot_js_app.dao;

import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.repositories.NoteRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addRows.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteRows.sql"),
})
public class NoteRepositoryTest {
    @Autowired
    private NoteRepository repository;

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addSpecialRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteSpecialRows.sql"),
    })
    public void findAllTest() {
        List<Note> list = repository.findAll();
        int i = 2;

        for (Note note: list) {
            assertEquals(note.getLogin(), "login" + i);
            i++;
        }
    }

    @Test
    public void findByIdTest() {
        assertTrue(repository.findById(1).isPresent());
        assertFalse(repository.findById(2).isPresent());
    }

    @Test
    public void getOneTest() {
        assertEquals(repository.getOne(1).getLogin(), "login");
        assertEquals(repository.getOne(2).getLogin(), "not a login");
    }

    @Test
    @Sql("/sqlScripts/notesTable/deleteSpecialRows.sql")
    public void saveTest() {
        Note note = repository.save(new Note("special", "briefDescription", "fullDescription", "special", "title"));
        assertEquals(note.getLogin(), "special");
    }

    @Test
    public void deleteByIdTest() {
        repository.deleteById(1);
        try {
            repository.deleteById(2);
        } catch (EmptyResultDataAccessException e) {
            assertTrue(true);
        }
    }
}
