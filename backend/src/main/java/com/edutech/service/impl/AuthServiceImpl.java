package com.edutech.service.impl;

import com.edutech.domain.*;
import com.edutech.dto.*;
import com.edutech.exception.ApiException;
import com.edutech.repository.CompanyProfileRepository;
import com.edutech.repository.RoleRepository;
import com.edutech.repository.StudentProfileRepository;
import com.edutech.repository.UserRepository;
import com.edutech.security.JwtService;
import com.edutech.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           StudentProfileRepository studentProfileRepository,
                           CompanyProfileRepository companyProfileRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.companyProfileRepository = companyProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public TokenResponse registerStudent(RegisterStudentRequest request) {
        User user = registerUser(request.email(), request.password(), RoleName.STUDENT);
        studentProfileRepository.save(StudentProfile.builder()
                .userId(user.getId())
                .fullName(request.fullName())
                .fieldOfStudy(request.fieldOfStudy())
                .location(request.location())
                .subjects(request.subjects())
                .grades(request.grades())
                .build());
        return tokensFor(user);
    }

    @Override
    @Transactional
    public TokenResponse registerCompany(RegisterCompanyRequest request) {
        User user = registerUser(request.email(), request.password(), RoleName.COMPANY);
        companyProfileRepository.save(CompanyProfile.builder()
                .userId(user.getId())
                .companyName(request.companyName())
                .industry(request.industry())
                .website(request.website())
                .build());
        return tokensFor(user);
    }

    private User registerUser(String email, String password, RoleName roleName) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Invalid role"));
        return userRepository.save(User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .roles(Set.of(role))
                .enabled(true)
                .build());
    }

    @Override
    public TokenResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        return tokensFor(user);
    }

    @Override
    public TokenResponse refresh(RefreshTokenRequest request) {
        if (!jwtService.isRefreshToken(request.refreshToken())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
        User user = userRepository.findByEmail(jwtService.extractSubject(request.refreshToken()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
        return tokensFor(user);
    }

    @Override
    public void logout(String authHeader) {
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
    }

    @Override
    public UserResponse me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserResponse(user.getId(), user.getEmail(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(java.util.stream.Collectors.toSet()));
    }

    private TokenResponse tokensFor(User user) {
        return new TokenResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user),
                "Bearer", jwtService.accessTokenExpirationSeconds());
    }
}
