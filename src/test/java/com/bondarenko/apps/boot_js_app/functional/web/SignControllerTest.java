package com.bondarenko.apps.boot_js_app.functional.web;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addRows.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteRows.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteSpecialRows.sql"),
})
public class SignControllerTest {
    @Autowired
    WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
    }

    @Ignore
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addHashedRows.sql")
    public void authorizeTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/login");
        request.param("login", "loginH")
                .param("password", "password");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isAuthorized").value("true"))
                .andExpect(jsonPath("$.name").value("special"))
                .andExpect(jsonPath("$.login").value("loginH"))
                .andExpect(jsonPath("$.email").value("login@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));


        request = MockMvcRequestBuilders.post("/login");
        request.param("login", "logan")
                .param("password", "password");
        result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isAuthorized").value("false"));


        request = MockMvcRequestBuilders.post("/login");
        request.param("login", "loginH")
                .param("password", "passworld");
        result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isAuthorized").value("false"));


        request = MockMvcRequestBuilders.post("/login");
        request.param("login", "logan")
                .param("password", "passworld");
        result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isAuthorized").value("false"));


        //Test without requested parameters
        request = MockMvcRequestBuilders.post("/login");
        result = mockMvc.perform(request);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void registerTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/register");
        request.param("login", "logan")
                .param("password", "password")
                .param("name", "special")
                .param("email", "logan@test.com");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isRegistered").value("true"));


        request = MockMvcRequestBuilders.post("/register");
        request.param("login", "login")
                .param("password", "password")
                .param("name", "special")
                .param("email", "login@test.com");
        result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isRegistered").value("false"));


        //Test without requested parameters
        request = MockMvcRequestBuilders.post("/register");
        result = mockMvc.perform(request);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void checkLoginTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/checkLogin");
        request.param("login", "login");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isExist").value("true"));


        request = MockMvcRequestBuilders.post("/checkLogin");
        request.param("login", "logan");
        result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isExist").value("false"));


        //Test without requested parameters
        request = MockMvcRequestBuilders.post("/checkLogin");
        result = mockMvc.perform(request);
        result.andExpect(status().isBadRequest());

    }

    @Test
    public void checkEmailTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/checkEmail");
        request.param("email", "login@test.com");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isExist").value("true"));


        request = MockMvcRequestBuilders.post("/checkEmail");
        request.param("email", "logan@test.com");
        result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isExist").value("false"));


        //Test without requested parameters
        request = MockMvcRequestBuilders.post("/checkEmail");
        result = mockMvc.perform(request);
        result.andExpect(status().isBadRequest());
    }
}
