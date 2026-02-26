package com.edutech.service.impl;

import com.edutech.domain.*;
import com.edutech.dto.RegisterStudentRequest;
import com.edutech.repository.*;
import com.edutech.service.AdminService;
import com.edutech.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final BursaryRepository bursaryRepository;
    private final PaymentRepository paymentRepository;
    private final AuthService authService;

    public AdminServiceImpl(UserRepository userRepository, BursaryRepository bursaryRepository,
                            PaymentRepository paymentRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.bursaryRepository = bursaryRepository;
        this.paymentRepository = paymentRepository;
        this.authService = authService;
    }

    @Override
    public List<User> users() {
        return userRepository.findAll();
    }

    @Override
    public Map<String, Object> importUsers(MultipartFile file) {
        int created = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].contains("@")) {
                    authService.registerStudent(new RegisterStudentRequest(parts[0], parts[1], "Imported", "Student",
                            "General", "Online", "Math", "A"));
                    created++;
                }
            }
        } catch (Exception ignored) {
        }
        return Map.of("imported", created);
    }

    @Override
    public List<Bursary> pendingBursaries() {
        return bursaryRepository.findByStatus(BursaryStatus.PENDING_APPROVAL);
    }

    @Override
    public Bursary approveBursary(Long id) {
        Bursary bursary = bursaryRepository.findById(id).orElseThrow();
        bursary.setStatus(BursaryStatus.ACTIVE);
        return bursaryRepository.save(bursary);
    }

    @Override
    public Bursary rejectBursary(Long id) {
        Bursary bursary = bursaryRepository.findById(id).orElseThrow();
        bursary.setStatus(BursaryStatus.REJECTED);
        return bursaryRepository.save(bursary);
    }

    @Override
    public Map<String, Object> userAnalytics() {
        return Map.of("totalUsers", userRepository.count());
    }

    @Override
    public Map<String, Object> revenueAnalytics() {
        return Map.of("paymentsCount", paymentRepository.count());
    }
}
