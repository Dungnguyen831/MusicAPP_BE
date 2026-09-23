package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 230)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PlaylistVisibility visibility = PlaylistVisibility.PRIVATE;

    @Enumerated(EnumType.STRING)
    @Column(name = "playlist_type", nullable = false)
    @Builder.Default
    private PlaylistType playlistType = PlaylistType.USER_CREATED;

    @Column(name = "total_songs", nullable = false)
    @Builder.Default
    private Integer totalSongs = 0;

    @Column(name = "total_duration_seconds", nullable = false)
    @Builder.Default
    private Long totalDurationSeconds = 0L;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

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

    public enum PlaylistVisibility {
        PUBLIC, PRIVATE, UNLISTED
    }

    public enum PlaylistType {
        USER_CREATED, SYSTEM, RECOMMENDED
    }
}
