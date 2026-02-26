package com.edutech.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @JsonAlias({"username", "userEmail"}) @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 128) String password
) {
}
