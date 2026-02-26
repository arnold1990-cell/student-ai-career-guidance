package com.edutech.service;

import com.edutech.dto.*;

public interface AuthService {
    TokenResponse registerStudent(RegisterStudentRequest request);

    TokenResponse registerCompany(RegisterCompanyRequest request);

    TokenResponse login(AuthRequest request);

    TokenResponse refresh(RefreshTokenRequest request);

    void logout(String authHeader);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    UserResponse me();
}
