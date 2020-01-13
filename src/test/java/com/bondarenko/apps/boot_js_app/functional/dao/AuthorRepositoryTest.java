package com.bondarenko.apps.boot_js_app.functional.dao;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.repositories.AuthorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addRows.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteRows.sql"),
})
public class AuthorRepositoryTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private AuthorRepository repository;

    @Test
    public void existsByIdTest() {
        assertTrue(repository.existsById("login")); // Positive test; condition: the author specified by login exists in DB.
        assertFalse(repository.existsById("not a login")); // Negative test; condition: the author specified by login does not exist in DB.
    }

    @Test
    public void existsAuthorByEmailTest() {
        assertTrue(repository.existsAuthorByEmail("login@test.com")); // Positive test; condition: the author specified by email exists in DB.
        assertFalse(repository.existsAuthorByEmail("not an email")); // Negative test; condition: the author specified by email does not exist in DB.
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteSpecialRows.sql")
    public void saveTest() {
        Author author = repository.save(new Author("special", "password", "special", "special@test.com", Author.READER));
        assertEquals(author.getLogin(), "special"); // Positive test; condition: the author has been saved to embedded DB.
        assertEquals(template.queryForMap("SELECT * FROM authors WHERE login = 'special'").get("login"), "special"); // Positive test; condition: the author has been saved to DB.
    }

    @Test
    public void findByIdTest() {
        assertTrue(repository.findById("login").isPresent()); // Positive test; condition: the author specified by login exists in DB.
        assertFalse(repository.findById("not a login").isPresent()); // Negative test; condition: the author specified by login does not exist in DB.
    }
}
