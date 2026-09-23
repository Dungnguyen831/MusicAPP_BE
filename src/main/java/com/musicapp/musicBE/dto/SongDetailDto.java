package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SongDetailDto {
    private Long id;
    private String title;
    private String slug;
    private Integer durationSeconds;
    private String audioUrl;
    private String coverImageUrl;
    private Long fileSizeBytes;
    private String mimeType;
    private Integer bitrate;
    private Long playCount;
    private Long likeCount;
    private Boolean isVipOnly;
    private Boolean isDownloadable;
    private LocalDate releaseDate;
    private List<ArtistDto> artists;
    private List<GenreDto> genres;
    private LyricDto lyrics;

    @Data
    @Builder
    public static class ArtistDto {
        private Long id;
        private String name;
        private String slug;
        private String avatarUrl;
    }

    @Data
    @Builder
    public static class GenreDto {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @Builder
    public static class LyricDto {
        private Long id;
        private String content;
        private String syncedLyrics;
        private String language;
    }
}
