package com.bondarenko.apps.boot_js_app.controlers;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestApiController {
    private int counter = 2;
    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{
                put("id", "1"); put("name", "Bodun");
        }});
        add(new HashMap<String, String>() {{
            put("id", "2"); put("name", "Andrew");
        }});
        add(new HashMap<String, String>() {{
            put("id", "3"); put("name", "Bugai");
        }});
    }};

    @GetMapping("/get")
    public List<Map<String, String>> get() {
        System.out.println("GET message/get");
        return messages;
    }

    @GetMapping("/{id}")
    public Map<String, String> getId(@PathVariable String id) {
        for (Map<String, String> map: messages) {
            if(map.containsValue(id)) {
                return map;
            }
        }
        return null;
    }

    @PostMapping
    public Map<String, String> post(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        return message;
    }

    //TODO: fix redirecting
    @GetMapping("/")
    public String getDefault() {
        System.out.println("GET message/");
        return "Main page!";
    }
}
