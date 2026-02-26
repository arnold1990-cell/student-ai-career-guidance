package com.edutech.service.impl;

import com.edutech.domain.*;
import com.edutech.dto.BursaryRequest;
import com.edutech.dto.MessageRequest;
import com.edutech.exception.ApiException;
import com.edutech.repository.*;
import com.edutech.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CurrentUserService currentUserService;
    private final CompanyProfileRepository companyProfileRepository;
    private final BursaryRepository bursaryRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final MessageRepository messageRepository;

    public CompanyServiceImpl(CurrentUserService currentUserService, CompanyProfileRepository companyProfileRepository,
                              BursaryRepository bursaryRepository, StudentProfileRepository studentProfileRepository,
                              MessageRepository messageRepository) {
        this.currentUserService = currentUserService;
        this.companyProfileRepository = companyProfileRepository;
        this.bursaryRepository = bursaryRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public Bursary createBursary(BursaryRequest request) {
        CompanyProfile profile = currentCompanyProfile();
        Bursary bursary = Bursary.builder()
                .companyId(profile.getId())
                .title(request.title())
                .fieldOfStudy(request.fieldOfStudy())
                .location(request.location())
                .eligibility(request.eligibility())
                .description(request.description())
                .amount(request.amount())
                .deadlineDate(request.deadlineDate())
                .status(BursaryStatus.PENDING_APPROVAL)
                .build();
        return bursaryRepository.save(bursary);
    }

    @Override
    @Transactional
    public Bursary updateBursary(Long id, BursaryRequest request) {
        Bursary bursary = bursaryRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bursary not found"));
        bursary.setTitle(request.title());
        bursary.setFieldOfStudy(request.fieldOfStudy());
        bursary.setLocation(request.location());
        bursary.setEligibility(request.eligibility());
        bursary.setDescription(request.description());
        bursary.setAmount(request.amount());
        bursary.setDeadlineDate(request.deadlineDate());
        bursary.setStatus(BursaryStatus.PENDING_APPROVAL);
        return bursaryRepository.save(bursary);
    }

    @Override
    public List<Map<String, Object>> searchStudents(String q) {
        String keyword = q == null ? "" : q.toLowerCase();
        return studentProfileRepository.findAll().stream()
                .filter(s -> s.getFieldOfStudy().toLowerCase().contains(keyword)
                        || s.getSubjects().toLowerCase().contains(keyword)
                        || s.getLocation().toLowerCase().contains(keyword))
                .map(s -> Map.<String, Object>of("id", s.getId(), "name", s.getFullName(), "field", s.getFieldOfStudy(),
                        "location", s.getLocation()))
                .toList();
    }

    @Override
    @Transactional
    public void sendMessage(MessageRequest request) {
        User sender = currentUserService.requireCurrentUser();
        messageRepository.save(Message.builder()
                .senderUserId(sender.getId())
                .recipientUserId(request.recipientUserId())
                .content(request.content())
                .build());
    }

    @Override
    public Map<String, Object> dashboard() {
        CompanyProfile companyProfile = currentCompanyProfile();
        long totalPosts = bursaryRepository.findAll().stream()
                .filter(b -> b.getCompanyId().equals(companyProfile.getId())).count();
        return Map.of("company", companyProfile.getCompanyName(), "bursariesPosted", totalPosts);
    }

    private CompanyProfile currentCompanyProfile() {
        return companyProfileRepository.findByUserId(currentUserService.requireCurrentUser().getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Company profile not found"));
    }
}
