package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private NotificationType type = NotificationType.SYSTEM;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    @Builder.Default
    private NotificationTargetType targetType = NotificationTargetType.NONE;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum NotificationType {
        SYSTEM, NEW_SONG, PLAYLIST, VIP, RECOMMENDATION
    }

    public enum NotificationTargetType {
        NONE, SONG, PLAYLIST, ALBUM, ARTIST
    }
}
