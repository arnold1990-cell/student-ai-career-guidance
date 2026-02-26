package com.edutech.repository;

import com.edutech.domain.Bursary;
import com.edutech.domain.BursaryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BursaryRepository extends JpaRepository<Bursary, Long> {
    List<Bursary> findByStatus(BursaryStatus status);
}
