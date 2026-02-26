package com.edutech.service;

import com.edutech.dto.RecommendationResponse;

public interface RecommendationService {
    RecommendationResponse recommendCareers(Long studentId);

    RecommendationResponse recommendBursaries(Long studentId);
}
