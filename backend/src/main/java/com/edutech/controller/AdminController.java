package com.edutech.controller;

import com.edutech.domain.Bursary;
import com.edutech.domain.User;
import com.edutech.service.AdminService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<User> users() {
        return adminService.users();
    }

    @PostMapping("/users/import")
    public Map<String, Object> importUsers(@RequestParam("file") MultipartFile file) {
        return adminService.importUsers(file);
    }

    @GetMapping("/bursaries/pending")
    public List<Bursary> pendingBursaries() {
        return adminService.pendingBursaries();
    }

    @PostMapping("/bursaries/{id}/approve")
    public Bursary approve(@PathVariable Long id) {
        return adminService.approveBursary(id);
    }

    @PostMapping("/bursaries/{id}/reject")
    public Bursary reject(@PathVariable Long id) {
        return adminService.rejectBursary(id);
    }

    @GetMapping("/analytics/users")
    public Map<String, Object> usersAnalytics() {
        return adminService.userAnalytics();
    }

    @GetMapping("/analytics/revenue")
    public Map<String, Object> revenueAnalytics() {
        return adminService.revenueAnalytics();
    }
}
