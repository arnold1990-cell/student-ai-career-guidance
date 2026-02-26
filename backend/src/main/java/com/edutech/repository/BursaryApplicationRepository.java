package com.edutech.repository;

import com.edutech.domain.BursaryApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BursaryApplicationRepository extends JpaRepository<BursaryApplication, Long> {
    long countByStudentId(Long studentId);
}
