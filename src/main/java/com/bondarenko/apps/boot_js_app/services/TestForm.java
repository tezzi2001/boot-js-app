package com.bondarenko.apps.boot_js_app.services;

import org.springframework.stereotype.Service;

@Service
public class TestForm implements ITestForm{
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
