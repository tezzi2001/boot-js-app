package com.bondarenko.apps.boot_js_app.service;

import com.bondarenko.apps.boot_js_app.services.ISignService;
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
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteSpecialRows.sql"),
})
public class SignServiceTest {
    @Autowired
    ISignService service;

    @Test
    public void registerTest() {
        assertFalse(service.register("login", "password", "special", "test1@gmail.com")); // Negative test; condition: current login exists in DB.
        assertFalse(service.register("logo", "password", "special", "login@test.com")); // Negative test; condition: current email exists in DB.
        assertTrue(service.register("logan", "password", "special", "test3@gmail.com")); // Positive test; condition: current login and email does not exist in DB.
    }

    @Test
    public void authorizeTest() {
        assertNull(service.authorize("logan", "password")); // Negative test; condition: current login does not match login in DB.
        assertNull(service.authorize("login", "passworld")); // Negative test; condition: current password does not match password in DB.
        assertNull(service.authorize("logan", "passworld")); // Negative test; condition: current login and password do not match login and password in DB.
        assertNotNull(service.authorize("login", "password")); // Positive test; condition: current login and password match login and password in DB.
    }

    @Test
    public void checkLoginTest() {
        assertFalse(service.checkLogin("logan")); // Negative test; condition: current login does not match login in DB.
        assertTrue(service.checkLogin("login")); // Positive test; condition: current login matches login in DB
    }

    @Test
    public void checkEmailTest() {
        assertFalse(service.checkEmail("logan@test.com")); // Negative test; condition: current email does not match email in DB.
        assertTrue(service.checkEmail("login@test.com")); // Positive test; condition: current email matches email in DB
    }
}
