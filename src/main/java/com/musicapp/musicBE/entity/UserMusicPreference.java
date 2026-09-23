package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_music_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMusicPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "artist_id")
    private Long artistId;

    @Column(nullable = false, precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal score = BigDecimal.ZERO;

    @Column(name = "last_calculated_at", nullable = false)
    @Builder.Default
    private LocalDateTime lastCalculatedAt = LocalDateTime.now();
}
