package com.edutech.repository;

import com.edutech.domain.BursaryBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BursaryBookmarkRepository extends JpaRepository<BursaryBookmark, Long> {
    long countByStudentId(Long studentId);
}
