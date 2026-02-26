package com.edutech.service;

import com.edutech.domain.Bursary;
import com.edutech.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AdminService {
    List<User> users();

    Map<String, Object> importUsers(MultipartFile file);

    List<Bursary> pendingBursaries();

    Bursary approveBursary(Long id);

    Bursary rejectBursary(Long id);

    Map<String, Object> userAnalytics();

    Map<String, Object> revenueAnalytics();
}
