package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lyrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lyric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "song_id", nullable = false, unique = true)
    private Long songId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "synced_lyrics", columnDefinition = "LONGTEXT")
    private String syncedLyrics;

    @Column(length = 30)
    @Builder.Default
    private String language = "vi";

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
}
