package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Gender gender = Gender.UNKNOWN;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 100)
    private String country;

    @Column(name = "preferred_language", length = 30)
    @Builder.Default
    private String preferredLanguage = "vi";

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_preference", nullable = false)
    @Builder.Default
    private ThemePreference themePreference = ThemePreference.SYSTEM;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

    public enum ThemePreference {
        LIGHT, DARK, SYSTEM
    }
}
