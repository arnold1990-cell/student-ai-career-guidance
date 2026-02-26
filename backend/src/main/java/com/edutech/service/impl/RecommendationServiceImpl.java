package com.edutech.service.impl;

import com.edutech.domain.Bursary;
import com.edutech.domain.StudentProfile;
import com.edutech.dto.RecommendationResponse;
import com.edutech.exception.ApiException;
import com.edutech.repository.BursaryRepository;
import com.edutech.repository.StudentProfileRepository;
import com.edutech.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final StudentProfileRepository studentProfileRepository;
    private final BursaryRepository bursaryRepository;

    public RecommendationServiceImpl(StudentProfileRepository studentProfileRepository, BursaryRepository bursaryRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.bursaryRepository = bursaryRepository;
    }

    @Override
    public RecommendationResponse recommendCareers(Long studentId) {
        StudentProfile profile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student profile not found"));
        List<String> careers = List.of(
                profile.getFieldOfStudy() + " Analyst",
                "Software Engineer",
                "Data Scientist",
                "Project Manager",
                "Operations Specialist");
        return new RecommendationResponse(careers, List.of(),
                List.of("Improve communication skills", "Build portfolio projects", "Gain internship experience"));
    }

    @Override
    public RecommendationResponse recommendBursaries(Long studentId) {
        StudentProfile profile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student profile not found"));

        List<Bursary> matches = bursaryRepository.findAll().stream()
                .filter(b -> b.getFieldOfStudy().equalsIgnoreCase(profile.getFieldOfStudy())
                        || b.getLocation().equalsIgnoreCase(profile.getLocation())
                        || b.getEligibility().toLowerCase().contains(profile.getGrades().toLowerCase()))
                .sorted(Comparator.comparing(Bursary::getDeadlineDate))
                .limit(10)
                .toList();

        return new RecommendationResponse(List.of(),
                matches.stream().map(Bursary::getId).toList(),
                List.of("Strengthen subject averages", "Add volunteering experience"));
    }
}
