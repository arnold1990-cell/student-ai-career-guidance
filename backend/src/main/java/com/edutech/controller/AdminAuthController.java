package com.edutech.controller;

import com.edutech.dto.AuthRequest;
import com.edutech.dto.TokenResponse;
import com.edutech.exception.ApiException;
import com.edutech.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenResponse adminLogin(@Valid @RequestBody AuthRequest request) {
        TokenResponse response = authService.login(request);
        if (response.roles().stream().noneMatch("ADMIN"::equals)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Admin role required");
        }
        return response;
    }
}
