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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
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
    public UserResponse registerStudent(RegisterStudentRequest request) {
        User user = registerUser(request.email(), request.password(), RoleName.STUDENT);
        studentProfileRepository.save(StudentProfile.builder()
                .userId(user.getId())
                .fullName("%s %s".formatted(request.firstName(), request.lastName()))
                .fieldOfStudy(request.fieldOfStudy())
                .location(request.location())
                .subjects(request.subjects())
                .grades(request.grades())
                .build());
        return toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse registerCompany(RegisterCompanyRequest request) {
        User user = registerUser(request.email(), request.password(), RoleName.COMPANY);
        companyProfileRepository.save(CompanyProfile.builder()
                .userId(user.getId())
                .companyName(request.companyName())
                .industry(request.industry())
                .website(request.website())
                .build());
        return toUserResponse(user);
    }

    private User registerUser(String email, String password, RoleName roleName) {
        String normalizedEmail = email.trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Invalid role"));
        return userRepository.save(User.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(password))
                .roles(Set.of(role))
                .enabled(true)
                .build());
    }

    @Override
    public TokenResponse login(AuthRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        userRepository.findByEmailIgnoreCase(normalizedEmail).ifPresentOrElse(
                user -> log.debug("Login candidate found for email={} enabled={} roles={} passwordHashPrefix={}",
                        normalizedEmail,
                        user.isEnabled(),
                        user.getRoles().stream().map(role -> role.getName().name()).toList(),
                        passwordHashPrefix(user.getPasswordHash())),
                () -> log.debug("Login candidate not found for email={}", normalizedEmail)
        );

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.password()));
            User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                    .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
            log.debug("Login successful for email={} roles={}", user.getEmail(),
                    user.getRoles().stream().map(role -> role.getName().name()).toList());
            return tokensFor(user);
        } catch (BadCredentialsException ex) {
            log.debug("Login rejected due to bad credentials for email={}", normalizedEmail);
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @Override
    public TokenResponse refresh(RefreshTokenRequest request) {
        if (!jwtService.isRefreshToken(request.refreshToken())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
        User user = userRepository.findByEmailIgnoreCase(jwtService.extractSubject(request.refreshToken()))
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
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        return toUserResponse(user);
    }

    private String passwordHashPrefix(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            return "<empty>";
        }
        int prefixLength = Math.min(passwordHash.length(), 4);
        return passwordHash.substring(0, prefixLength);
    }

    private TokenResponse tokensFor(User user) {
        Set<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        return new TokenResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user),
                "Bearer", jwtService.accessTokenExpirationSeconds(), roles, user.getEmail());
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }
}
