package com.edutech.dto;

import com.edutech.domain.SubscriptionTier;
import jakarta.validation.constraints.NotNull;

public record SubscriptionRequest(@NotNull SubscriptionTier tier) {
}
