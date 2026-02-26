package com.edutech.controller;

import com.edutech.domain.Bursary;
import com.edutech.dto.BursaryRequest;
import com.edutech.dto.MessageRequest;
import com.edutech.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/bursaries")
    public Bursary createBursary(@Valid @RequestBody BursaryRequest request) {
        return companyService.createBursary(request);
    }

    @PutMapping("/bursaries/{id}")
    public Bursary updateBursary(@PathVariable Long id, @Valid @RequestBody BursaryRequest request) {
        return companyService.updateBursary(id, request);
    }

    @GetMapping("/students/search")
    public List<Map<String, Object>> searchStudents(@RequestParam(required = false) String q) {
        return companyService.searchStudents(q);
    }

    @PostMapping("/messages")
    public Map<String, String> sendMessage(@Valid @RequestBody MessageRequest request) {
        companyService.sendMessage(request);
        return Map.of("message", "Message sent");
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return companyService.dashboard();
    }
}
