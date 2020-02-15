package com.bondarenko.apps.boot_js_app.functional.dao;

import com.bondarenko.apps.boot_js_app.domain.entities.Note;
import com.bondarenko.apps.boot_js_app.repositories.NoteRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addRows.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteRows.sql"),
})
@ActiveProfiles("dev")
public class NoteRepositoryTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private NoteRepository repository;

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addSpecialRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteSpecialRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteRows.sql"),
    })
    public void findAllTest() {
        List<Note> list = repository.findAll();
        int i = 2;

        for (Note note: list) {
            assertEquals(note.getBriefDescription(), "brief_description" + i); // Positive test; condition: current briefDescription exists in DB.
            i++;
        }
    }

    @Test
    public void findByIdTest() {
        assertTrue(repository.findById(1).isPresent()); // Positive test; condition: current login specified by id exists in DB.
        assertFalse(repository.findById(2).isPresent()); // Negative test; condition: current login specified by id does not exist in DB.
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteSpecialRows.sql")
    public void saveTest() {
        Note note = repository.save(new Note("briefDescriptionForSave", "fullDescription", new Date(), "special", 0));
        assertEquals(note.getBriefDescription(), "briefDescriptionForSave"); // Positive test; condition: the note has been saved to embedded DB.
        assertEquals(template.queryForMap("SELECT brief_description FROM notes WHERE brief_description = 'briefDescriptionForSave'").get("brief_description"), "briefDescriptionForSave"); // Positive test; condition: the note has been saved to DB.
    }

    @Test
    public void deleteByIdTest() {
        repository.deleteById(1);
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> template.queryForMap("SELECT * FROM notes WHERE id = 1")); // Positive test; condition: the note specified by id has been deleted from DB.
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(2)); // Negative test; condition: the note specified by id has not been deleted from DB.
    }
}
