package com.edutech.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BursaryRequest(
        @NotBlank String title,
        @NotBlank String fieldOfStudy,
        @NotBlank String location,
        @NotBlank String eligibility,
        @NotBlank String description,
        @NotNull @DecimalMin("0.0") BigDecimal amount,
        @NotNull LocalDate deadlineDate
) {
}
