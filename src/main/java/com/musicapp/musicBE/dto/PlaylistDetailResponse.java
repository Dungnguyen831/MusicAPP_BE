package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PlaylistDetailResponse {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String coverImageUrl;
    private String visibility;
    private String playlistType;
    private Integer totalSongs;
    private Long totalDurationSeconds;
    private List<SongDto> songs;

    @Data
    @Builder
    public static class SongDto {
        private Long id;
        private String title;
        private String slug;
        private Integer durationSeconds;
        private String audioUrl;
        private String coverImageUrl;
        private Long playCount;
        private Long likeCount;
        private Boolean isVipOnly;
        private Boolean isDownloadable;
        private List<ArtistDto> artists;
    }

    @Data
    @Builder
    public static class ArtistDto {
        private Long id;
        private String name;
        private String slug;
    }
}
