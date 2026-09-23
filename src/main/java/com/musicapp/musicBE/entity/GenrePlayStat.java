package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "genre_play_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenrePlayStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genre_id", nullable = false)
    private Long genreId;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "play_count", nullable = false)
    @Builder.Default
    private Long playCount = 0L;

    @Column(name = "unique_listener_count", nullable = false)
    @Builder.Default
    private Long uniqueListenerCount = 0L;
}
