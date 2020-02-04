package com.bondarenko.apps.boot_js_app.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class JWTTest {
    @Autowired
    WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addHashedRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteSpecialRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/tokensTable/addHashedRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/tokensTable/deleteRows.sql"),
    })
    public void testOnAuthentication() throws Exception{
        ResultActions result;

        MockHttpServletRequestBuilder successfulRequest = MockMvcRequestBuilders.post("/login");
        successfulRequest.param("login", "loginH")
                .param("password", "password")
                .param("fingerprint", "test");
        result = mockMvc.perform(successfulRequest);
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());

        MockHttpServletRequestBuilder nullRequest = MockMvcRequestBuilders.post("/login");
        result = mockMvc.perform(nullRequest);
        result.andExpect(status().isBadRequest());

        MockHttpServletRequestBuilder wrongLoginRequest = MockMvcRequestBuilders.post("/login");
        wrongLoginRequest.param("login", "wrongLogin")
                .param("password", "password")
                .param("fingerprint", "test");
        result = mockMvc.perform(wrongLoginRequest);
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.status").value("INVALID_USER"));

        MockHttpServletRequestBuilder wrongPasswordRequest = MockMvcRequestBuilders.post("/login");
        wrongPasswordRequest.param("login", "loginH")
                .param("password", "wrongPassword")
                .param("fingerprint", "test");
        result = mockMvc.perform(wrongPasswordRequest);
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.status").value("INVALID_USER"));
    }
}
