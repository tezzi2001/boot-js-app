package com.bondarenko.apps.boot_js_app.controlers;

import com.bondarenko.apps.boot_js_app.services.ITestForm;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class RestApiController {
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

    //TODO: fix redirecting
    @GetMapping("/")
    public String getDefault() {
        System.out.println("GET message/");
        return "Main page!";
    }

    @GetMapping(value = "/form", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HashMap<String, String> service(@RequestBody ITestForm testForm){
        return new HashMap<String, String>() {{
            put("Name", testForm.getName());
            put("Age", testForm.getAge().toString());
        }};
    }
}
