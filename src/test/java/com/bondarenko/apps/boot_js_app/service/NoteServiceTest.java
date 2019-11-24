package com.bondarenko.apps.boot_js_app.service;

import com.bondarenko.apps.boot_js_app.entities.Note;
import com.bondarenko.apps.boot_js_app.services.INoteService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteServiceTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    INoteService service;

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addSpecialRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteSpecialRows.sql"),
    })
    public void getNotesTest() {
        List<Note> list = service.getNotes();
        int i = 2;

        for (Note note: list) {
            assertEquals(note.getBriefDescription(), "brief_description" + i); // Positive test; condition: notes exist in DB.
            i++;
        }

    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addSpecialRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteSpecialRows.sql"),
    })
    public void getNotesByIdTest() {
        for (int i = 2; i <= 5; i++) {
            assertEquals(service.getNoteById(i).getBriefDescription(), "brief_description" + i); // Positive test; condition: current note exists in DB.
        }
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteSpecialRows.sql"),
    })
    public void addNoteTest() {
        assertTrue(service.addNote(new Note("br_desc", "fll_desc", "special"), "login")); // Positive test; condition: current login exists in DB.
        assertFalse(service.addNote(new Note("br_desc", "fll_desc", "special"), "login")); // Negative test; condition: current login does not exist in DB.
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteRows.sql"),
    })
    public void deleteTest() {
        service.delete(1);
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> template.queryForMap("SELECT * FROM notes WHERE id = 1")); // Positive test; condition: the note specified by id has been deleted in DB;
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> service.delete(2)); // Negative test; condition: the note specified by id has not been deleted in DB;
    }
}
