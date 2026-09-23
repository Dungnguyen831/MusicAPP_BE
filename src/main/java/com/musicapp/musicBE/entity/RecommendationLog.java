package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "song_id", nullable = false)
    private Long songId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationAction action;

    @Column(length = 100)
    private String context;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RecommendationAction {
        SHOWN, CLICKED, PLAYED, SKIPPED, LIKED
    }
}
