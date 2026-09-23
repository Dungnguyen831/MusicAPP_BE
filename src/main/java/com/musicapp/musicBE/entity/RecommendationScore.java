package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "song_id", nullable = false)
    private Long songId;

    @Column(nullable = false, precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal score = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RecommendationReason reason = RecommendationReason.HYBRID;

    @Column(name = "generated_at", nullable = false)
    @Builder.Default
    private LocalDateTime generatedAt = LocalDateTime.now();

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public enum RecommendationReason {
        GENRE_MATCH, ARTIST_MATCH, HISTORY, FAVORITE, TRENDING, SIMILAR_SONG, HYBRID
    }
}
