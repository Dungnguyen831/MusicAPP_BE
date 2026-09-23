package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "song_similarity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongSimilarity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_song_id", nullable = false)
    private Long sourceSongId;

    @Column(name = "similar_song_id", nullable = false)
    private Long similarSongId;

    @Column(name = "similarity_score", nullable = false, precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal similarityScore = BigDecimal.ZERO;

    @Column(nullable = false, length = 100)
    @Builder.Default
    private String algorithm = "RULE_BASED";

    @Column(name = "calculated_at", nullable = false)
    @Builder.Default
    private LocalDateTime calculatedAt = LocalDateTime.now();
}
