package com.edutech.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @GetMapping("/dashboard")
    public Map<String, String> dashboard() {
        return Map.of("message", "Student dashboard endpoint");
    }
}
