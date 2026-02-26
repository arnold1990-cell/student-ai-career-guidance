package com.edutech.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "bursary_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BursaryApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bursary_id", nullable = false)
    private Long bursaryId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String status;

    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    @PrePersist
    public void onCreate() {
        if (appliedAt == null) {
            appliedAt = Instant.now();
        }
        if (status == null) {
            status = "SUBMITTED";
        }
    }
}
