package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "song_play_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongPlayStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "song_id", nullable = false)
    private Long songId;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "play_count", nullable = false)
    @Builder.Default
    private Long playCount = 0L;

    @Column(name = "unique_listener_count", nullable = false)
    @Builder.Default
    private Long uniqueListenerCount = 0L;

    @Column(name = "total_listened_seconds", nullable = false)
    @Builder.Default
    private Long totalListenedSeconds = 0L;
}
