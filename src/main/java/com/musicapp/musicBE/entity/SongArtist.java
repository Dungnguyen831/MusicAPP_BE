package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "song_artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SongArtistId.class)
public class SongArtist {
    @Id
    @Column(name = "song_id")
    private Long songId;

    @Id
    @Column(name = "artist_id")
    private Long artistId;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtistRole role;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ArtistRole {
        MAIN, FEATURED, COMPOSER, PRODUCER
    }
}
