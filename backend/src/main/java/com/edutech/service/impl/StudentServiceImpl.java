package com.edutech.service.impl;

import com.edutech.domain.*;
import com.edutech.dto.RecommendationResponse;
import com.edutech.dto.SubscriptionRequest;
import com.edutech.exception.ApiException;
import com.edutech.repository.*;
import com.edutech.service.RecommendationService;
import com.edutech.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {
    private final CurrentUserService currentUserService;
    private final StudentProfileRepository studentProfileRepository;
    private final BursaryRepository bursaryRepository;
    private final BursaryBookmarkRepository bookmarkRepository;
    private final BursaryApplicationRepository applicationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final RecommendationService recommendationService;

    public StudentServiceImpl(CurrentUserService currentUserService, StudentProfileRepository studentProfileRepository,
                              BursaryRepository bursaryRepository, BursaryBookmarkRepository bookmarkRepository,
                              BursaryApplicationRepository applicationRepository, SubscriptionRepository subscriptionRepository,
                              PaymentRepository paymentRepository, RecommendationService recommendationService) {
        this.currentUserService = currentUserService;
        this.studentProfileRepository = studentProfileRepository;
        this.bursaryRepository = bursaryRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.applicationRepository = applicationRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentRepository = paymentRepository;
        this.recommendationService = recommendationService;
    }

    @Override
    public Map<String, Object> dashboard() {
        User user = currentUserService.requireCurrentUser();
        StudentProfile profile = studentProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student profile not found"));
        return Map.of("student", profile.getFullName(), "bookmarks", bookmarkRepository.countByStudentId(profile.getId()),
                "applications", applicationRepository.countByStudentId(profile.getId()));
    }

    @Override
    public List<Bursary> listBursaries() {
        return bursaryRepository.findAll();
    }

    @Override
    @Transactional
    public void bookmarkBursary(Long bursaryId) {
        Long studentId = currentProfile().getId();
        bookmarkRepository.save(BursaryBookmark.builder().bursaryId(bursaryId).studentId(studentId).build());
    }

    @Override
    @Transactional
    public void applyBursary(Long bursaryId) {
        Long studentId = currentProfile().getId();
        applicationRepository.save(BursaryApplication.builder().bursaryId(bursaryId).studentId(studentId).build());
    }

    @Override
    public RecommendationResponse careerRecommendations() {
        return recommendationService.recommendCareers(currentProfile().getId());
    }

    @Override
    public RecommendationResponse bursaryRecommendations() {
        return recommendationService.recommendBursaries(currentProfile().getId());
    }

    @Override
    @Transactional
    public Map<String, Object> purchaseSubscription(SubscriptionRequest request) {
        User user = currentUserService.requireCurrentUser();
        Subscription subscription = subscriptionRepository.save(Subscription.builder()
                .userId(user.getId())
                .tier(request.tier())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .build());
        paymentRepository.save(Payment.builder().subscriptionId(subscription.getId())
                .amount(request.tier() == SubscriptionTier.PREMIUM ? new BigDecimal("199.00") : new BigDecimal("0.00"))
                .provider("MOCK")
                .status("SUCCESS")
                .build());
        return Map.of("subscriptionId", subscription.getId(), "tier", request.tier(), "provider", "mock");
    }

    private StudentProfile currentProfile() {
        return studentProfileRepository.findByUserId(currentUserService.requireCurrentUser().getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student profile not found"));
    }
}
