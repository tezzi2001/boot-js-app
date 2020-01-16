package com.bondarenko.apps.boot_js_app.functional.dao;

import com.bondarenko.apps.boot_js_app.repositories.JWTRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTRepositoryTest {
    @Autowired
    private JWTRepository repository;
    @Autowired
    private JdbcTemplate template;

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/tokensTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/tokensTable/deleteRows.sql")
    })
    public void deleteJWTByRefreshTokenTest() {
        assertNotNull(repository.deleteJWTByRefreshToken("testToken"));
        assertNull(repository.deleteJWTByRefreshToken("null"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/tokensTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/tokensTable/deleteRows.sql")
    })
    public void existsJWTByFingerprintTest() {
        assertTrue(repository.existsJWTByFingerprint("test"));
        assertFalse(repository.existsJWTByFingerprint("false"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/tokensTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/tokensTable/deleteRows.sql")
    })
    public void deleteJWTByFingerprintTest() {
        repository.deleteJWTByFingerprint("test");
        assertThrows(Exception.class, () -> template.queryForMap("SELECT * FROM tokens WHERE fingerprint = 'test'"));
        assertThrows(Exception.class, () -> template.queryForMap("SELECT * FROM tokens WHERE fingerprint = 'null'"));
        repository.deleteJWTByFingerprint("null");
        assertThrows(Exception.class, () -> template.queryForMap("SELECT * FROM tokens WHERE fingerprint = 'null'"));
    }
}
