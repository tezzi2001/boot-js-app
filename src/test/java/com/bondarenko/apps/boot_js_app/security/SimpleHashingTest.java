package com.bondarenko.apps.boot_js_app.security;

import com.bondarenko.apps.boot_js_app.services.ISignService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleHashingTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private ISignService service;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteSpecialRows.sql")
    public void testHash() throws Exception {
        String password = "password";
        service.register("loginH", password, "special", "test@spring.com");
        assertNotEquals(password, template.queryForMap("SELECT password FROM authors WHERE login = 'loginH'").get("password"));
    }
}
