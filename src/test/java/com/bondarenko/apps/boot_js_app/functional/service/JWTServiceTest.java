package com.bondarenko.apps.boot_js_app.functional.service;

import com.bondarenko.apps.boot_js_app.services.JWTService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
    public void refreshTokensNegativeTest() {
        assertEquals("NULL_FIELD", service.refreshTokens(null, null).get("status"));
        assertEquals("INVALID_SESSION", service.refreshTokens("testToken", "fingerprint").get("status"));
        assertEquals("INVALID_TOKEN", service.refreshTokens("invalidToken", "fingerprint").get("status"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/tokensTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/tokensTable/deleteRows.sql"),
    })
    public void refreshTokensNegativeOnExpireTest() throws InterruptedException {
        Thread.sleep(10000);
        assertEquals("TOKEN_EXPIRED", service.refreshTokens("testToken", "test").get("status"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/tokensTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/tokensTable/deleteRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteRows.sql"),
    })
    public void refreshTokensPositiveTest() {
        Map result = service.refreshTokens("testToken", "test");
        assertEquals("OK", result.get("status"));
        assertEquals(result.get("refreshToken"), template.queryForMap("SELECT * FROM tokens WHERE login = 'login'").get("refresh_token"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/tokensTable/addHashedRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addHashedRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteSpecialRows.sql"),
    })
    public void getTokensPositiveTest() {
        Map result = service.getTokens("loginH", "password", "test");
        assertEquals("OK", result.get("status"));
        assertEquals(result.get("refreshToken"), template.queryForMap("SELECT * FROM tokens WHERE login = 'loginH'").get("refresh_token"));
    }

    @Test
    public void getAuthorFromTokenNegativeTest() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        service.getAuthorFromToken("invalid_token");
        assertEquals("Invalid signature" + System.lineSeparator(), outContent.toString());
        System.setOut(System.out);
    }

}
