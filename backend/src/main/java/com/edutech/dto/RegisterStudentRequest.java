package com.edutech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterStudentRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 128) String password,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String fieldOfStudy,
        String location,
        String subjects,
        String grades
) {
}
