package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listening_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListeningHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "song_id", nullable = false)
    private Long songId;

    @Column(name = "device_id")
    private Long deviceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    @Builder.Default
    private HistorySourceType sourceType = HistorySourceType.UNKNOWN;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "listened_seconds", nullable = false)
    @Builder.Default
    private Integer listenedSeconds = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;

    @Column(name = "listened_at", nullable = false)
    @Builder.Default
    private LocalDateTime listenedAt = LocalDateTime.now();

    public enum HistorySourceType {
        SEARCH, PLAYLIST, RECOMMENDATION, ALBUM, ARTIST, HOME, UNKNOWN
    }
}
