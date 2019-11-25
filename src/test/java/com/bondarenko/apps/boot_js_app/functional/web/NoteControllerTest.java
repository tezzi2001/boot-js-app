package com.bondarenko.apps.boot_js_app.functional.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class NoteControllerTest {
    @Autowired
    WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addSpecialRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteSpecialRows.sql"),
    })
    public void getNotesTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/getAll");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].title").value("special"));
        result.andExpect(jsonPath("$[0].date").value("2019-11-18T22:00:00.000+0000"))
                .andExpect(jsonPath("$[0].briefDescription").value("brief_description2"))
                .andExpect(jsonPath("$[0].fullDescription").value("full_description2"));
        result.andExpect(jsonPath("$[1].title").value("special"))
                .andExpect(jsonPath("$[1].date").value("2019-11-18T22:00:00.000+0000"))
                .andExpect(jsonPath("$[1].briefDescription").value("brief_description3"))
                .andExpect(jsonPath("$[1].fullDescription").value("full_description3"));
        result.andExpect(jsonPath("$[2].title").value("special"))
                .andExpect(jsonPath("$[2].date").value("2019-11-18T22:00:00.000+0000"))
                .andExpect(jsonPath("$[2].briefDescription").value("brief_description4"))
                .andExpect(jsonPath("$[2].fullDescription").value("full_description4"));
        result.andExpect(jsonPath("$[3].title").value("special"))
                .andExpect(jsonPath("$[3].date").value("2019-11-18T22:00:00.000+0000"))
                .andExpect(jsonPath("$[3].briefDescription").value("brief_description5"))
                .andExpect(jsonPath("$[3].fullDescription").value("full_description5"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteRows.sql"),
    })
    public void getNoteTest() throws Exception {
        MockHttpServletRequestBuilder requestPositive = MockMvcRequestBuilders.get("/get1");
        ResultActions result = mockMvc.perform(requestPositive);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.date").value("2019-11-18T22:00:00.000+0000"))
                .andExpect(jsonPath("$.briefDescription").value("brief_description1"))
                .andExpect(jsonPath("$.fullDescription").value("full_description1"));

        MockHttpServletRequestBuilder requestNegative = MockMvcRequestBuilders.get("/get2");
        result = mockMvc.perform(requestNegative);
        result.andExpect(status().isBadRequest());


        //Test without requested parameters
        requestNegative = MockMvcRequestBuilders.get("/get");
        result = mockMvc.perform(requestNegative);
        result.andExpect(status().isBadRequest());
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/authorsTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteSpecialRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/authorsTable/deleteRows.sql"),
    })
    public void addTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/add");
        request.param("title", "special")
                .param("login", "borov")
                .param("briefDescription", "positive test")
                .param("fullDescription", "test of add() method");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isAdded").value("false"));

        request = MockMvcRequestBuilders.post("/add");
        request.param("title", "special")
                .param("login", "login")
                .param("briefDescription", "test")
                .param("fullDescription", "test of add() method");
        result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isAdded").value("true"));


        //Test without requested parameters
        request = MockMvcRequestBuilders.post("/add");
        result = mockMvc.perform(request);
        result.andExpect(status().isBadRequest());
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sqlScripts/notesTable/addRows.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sqlScripts/notesTable/deleteRows.sql"),
    })
    public void deleteTest() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/delete1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.isDeleted").value("true"));


        request = MockMvcRequestBuilders.delete("/delete2");
        result = mockMvc.perform(request);
        result.andExpect(status().isBadRequest());


        //Test without requested parameters
        request = MockMvcRequestBuilders.delete("/delete");
        result = mockMvc.perform(request);
        result.andExpect(status().isBadRequest());
    }
}
