package com.edutech.dto;

import java.util.Set;

public record TokenResponse(String accessToken,
                            String refreshToken,
                            String tokenType,
                            long expiresInSeconds,
                            Set<String> roles,
                            String email) {
}
