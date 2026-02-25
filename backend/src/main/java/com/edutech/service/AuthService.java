package com.edutech.service;

import com.edutech.dto.*;

public interface AuthService {
    TokenResponse register(RegisterRequest request);

    TokenResponse login(AuthRequest request);

    TokenResponse refresh(RefreshTokenRequest request);

    UserResponse me();
}
