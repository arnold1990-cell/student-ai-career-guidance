package com.edutech.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String subjects;

    @Column(nullable = false)
    private String grades;

    @Column(nullable = false)
    private String fieldOfStudy;

    @Column(nullable = false)
    private String location;

    private String cvUrl;
    private String transcriptUrl;
    private String qualifications;
    private String experience;
}
