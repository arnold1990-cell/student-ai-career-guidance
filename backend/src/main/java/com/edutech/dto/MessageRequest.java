package com.edutech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(@NotNull Long recipientUserId, @NotBlank String content) {
}
