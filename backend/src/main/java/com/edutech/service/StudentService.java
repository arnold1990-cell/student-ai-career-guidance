package com.edutech.service;

import com.edutech.domain.Bursary;
import com.edutech.dto.RecommendationResponse;
import com.edutech.dto.SubscriptionRequest;

import java.util.List;
import java.util.Map;

public interface StudentService {
    Map<String, Object> dashboard();

    List<Bursary> listBursaries();

    void bookmarkBursary(Long bursaryId);

    void applyBursary(Long bursaryId);

    RecommendationResponse careerRecommendations();

    RecommendationResponse bursaryRecommendations();

    Map<String, Object> purchaseSubscription(SubscriptionRequest request);
}
