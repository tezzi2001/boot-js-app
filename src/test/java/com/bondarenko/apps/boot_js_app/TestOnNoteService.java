package com.bondarenko.apps.boot_js_app;

import com.bondarenko.apps.boot_js_app.services.INoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOnNoteService {
    @Autowired
    private INoteService noteService;

    @Test
    public void test1() {
        assertEquals(noteService.getNotes(), new ArrayList());
    }
}
