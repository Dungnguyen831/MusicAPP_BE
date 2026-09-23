package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class HomeDataResponse {
    private List<BannerItemDto> banners;
    private List<SongDto> popularSongs;
    private List<CategoryDto> categories;
    private List<PlaylistDto> recommendedPlaylists;

    @Data
    @Builder
    public static class BannerItemDto {
        private Long id;
        private String title;
        private String subtitle;
        private String ctaText;
        private String backgroundImageUrl;
        private String targetType;
        private Long targetId;
    }

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
        private String avatarUrl;
    }

    @Data
    @Builder
    public static class CategoryDto {
        private Long id;
        private String name;
        private String slug;
        private String coverImageUrl;
    }

    @Data
    @Builder
    public static class PlaylistDto {
        private Long id;
        private String title;
        private String slug;
        private String description;
        private String coverImageUrl;
        private String visibility;
        private String playlistType;
        private Integer totalSongs;
        private Long totalDurationSeconds;
    }
}

