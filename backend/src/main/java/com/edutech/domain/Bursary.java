package com.edutech.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "bursaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bursary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String fieldOfStudy;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String eligibility;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "deadline_date", nullable = false)
    private LocalDate deadlineDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BursaryStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void beforeCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = BursaryStatus.PENDING_APPROVAL;
        }
    }

    @PreUpdate
    public void beforeUpdate() {
        updatedAt = Instant.now();
    }
}
