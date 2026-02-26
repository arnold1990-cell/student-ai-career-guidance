package com.edutech.controller;

import com.edutech.domain.Bursary;
import com.edutech.dto.RecommendationResponse;
import com.edutech.dto.SubscriptionRequest;
import com.edutech.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/api/student/dashboard")
    public Map<String, Object> dashboard() {
        return studentService.dashboard();
    }

    @GetMapping("/api/bursaries")
    public List<Bursary> bursaries() {
        return studentService.listBursaries();
    }

    @PostMapping("/api/student/bursaries/{id}/bookmark")
    public Map<String, String> bookmark(@PathVariable Long id) {
        studentService.bookmarkBursary(id);
        return Map.of("message", "Bookmarked");
    }

    @PostMapping("/api/student/bursaries/{id}/apply")
    public Map<String, String> apply(@PathVariable Long id) {
        studentService.applyBursary(id);
        return Map.of("message", "Application submitted");
    }

    @GetMapping("/api/student/recommendations/careers")
    public RecommendationResponse careerRecommendations() {
        return studentService.careerRecommendations();
    }

    @GetMapping("/api/student/recommendations/bursaries")
    public RecommendationResponse bursaryRecommendations() {
        return studentService.bursaryRecommendations();
    }

    @PostMapping("/api/student/subscription")
    public Map<String, Object> subscribe(@Valid @RequestBody SubscriptionRequest request) {
        return studentService.purchaseSubscription(request);
    }
}
