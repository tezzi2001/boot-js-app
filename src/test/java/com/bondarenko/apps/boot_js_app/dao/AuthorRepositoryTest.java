package com.bondarenko.apps.boot_js_app.dao;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.repositories.AuthorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private AuthorRepository repository;

    @Test
    public void existsByIdTest() {
        assertTrue(repository.existsById("login"));
        assertFalse(repository.existsById("not a login"));
    }

    @Test
    public void existsAuthorByEmailTest() {
        assertTrue(repository.existsAuthorByEmail("login@test.com"));
        assertFalse(repository.existsAuthorByEmail("not an email"));
    }

    @Test
    public void saveTest() {
        Author author = repository.save(new Author("special", "password", "special", "special@test.com"));
        assertEquals(author.getLogin(), "special");
    }

    @Test
    public void findByIdTest() {
        assertTrue(repository.findById("login").isPresent());
        assertFalse(repository.findById("not a login").isPresent());
    }
}
