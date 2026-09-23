package com.musicapp.musicBE.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "song_genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SongGenreId.class)
public class SongGenre {
    @Id
    @Column(name = "song_id")
    private Long songId;

    @Id
    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
