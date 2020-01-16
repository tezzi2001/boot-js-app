package com.bondarenko.apps.boot_js_app.functional.service;

import com.bondarenko.apps.boot_js_app.services.JWTService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/tokensTable/addRows.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/tokensTable/deleteRows.sql"),
})
public class JWTServiceTest {
    @Autowired
    private JWTService service;
    @Autowired
    private JdbcTemplate template;

    @Test
    public void refreshTokensNegativeTest() throws InterruptedException {
        assertEquals("NULL_FIELD", service.refreshTokens(null, null).get("status"));
        assertEquals("INVALID_SESSION", service.refreshTokens("testToken", "fingerprint").get("status"));
        assertEquals("INVALID_TOKEN", service.refreshTokens("invalidToken", "fingerprint").get("status"));
        wait(10000);
        assertEquals("TOKEN_EXPIRED", service.refreshTokens("testToken", "test").get("status"));
    }

    @Test
    public void refreshTokensPositiveTest() {
        Map result = service.refreshTokens("testToken", "test");
        assertEquals("OK", result.get("status"));
        assertEquals(result.get("refreshToken"), template.queryForMap("SELECT * FROM tokens WHERE login = 'login'").get("refresh_token"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteRows.sql"),
    })
    public void getTokensPositiveTest() {
        Map result = service.getTokens("login", "password", "test");
        assertEquals("OK", result.get("status"));
        assertEquals(result.get("refreshToken"), template.queryForMap("SELECT * FROM tokens WHERE login = 'login'").get("refresh_token"));
    }

    @Test
    public void getAuthorFromTokenNegativeTest() {
        assertThrows(Exception.class, () -> service.getAuthorFromToken("invalid_token"));
    }

}
