package com.edutech.service;

import com.edutech.domain.Bursary;
import com.edutech.dto.BursaryRequest;
import com.edutech.dto.MessageRequest;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    Bursary createBursary(BursaryRequest request);

    Bursary updateBursary(Long id, BursaryRequest request);

    List<Map<String, Object>> searchStudents(String q);

    void sendMessage(MessageRequest request);

    Map<String, Object> dashboard();
}
