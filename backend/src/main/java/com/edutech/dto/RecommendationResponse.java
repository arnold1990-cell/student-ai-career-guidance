package com.edutech.dto;

import java.util.List;

public record RecommendationResponse(
        List<String> careers,
        List<Long> bursaryIds,
        List<String> skillGapSuggestions
) {
}
