package com.musicapp.musicBE.controller;

import com.musicapp.musicBE.dto.ApiResponse;
import com.musicapp.musicBE.dto.HomeDataResponse;
import com.musicapp.musicBE.entity.BannerItem;
import com.musicapp.musicBE.entity.Genre;
import com.musicapp.musicBE.entity.Playlist;
import com.musicapp.musicBE.entity.Song;
import com.musicapp.musicBE.repository.BannerRepository;
import com.musicapp.musicBE.repository.GenreRepository;
import com.musicapp.musicBE.repository.PlaylistRepository;
import com.musicapp.musicBE.repository.SongRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Home Dashboard API.
 * <p>
 * Provides aggregated data for the app home screen.
 */
@Tag(name = "Home Dashboard", description = "APIs cung cấp dữ liệu tổng hợp cho trang chủ ứng dụng")
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    private final BannerRepository bannerRepository;
    private final SongRepository songRepository;
    private final GenreRepository genreRepository;
    private final PlaylistRepository playlistRepository;

    public HomeController(BannerRepository bannerRepository, SongRepository songRepository,
                          GenreRepository genreRepository, PlaylistRepository playlistRepository) {
        this.bannerRepository = bannerRepository;
        this.songRepository = songRepository;
        this.genreRepository = genreRepository;
        this.playlistRepository = playlistRepository;
    }

    /**
     * GET /api/v1/home/data
     * <p>
     * Returns banners, popular songs, categories (genres), and recommended playlists.
     *
     * @return {@code { "success": true, "data": { banners, popularSongs, categories, recommendedPlaylists } }}
     */
    @Operation(summary = "Lấy dữ liệu trang chủ", description = "Trả về danh sách banners, bài hát phổ biến, thể loại nhạc và các playlist được đề xuất")
    @GetMapping("/data")
    public ResponseEntity<ApiResponse<HomeDataResponse>> getHomeData() {

        List<BannerItem> banners = bannerRepository.findAll();

        // Lấy top 20 bài hát PUBLISHED sắp xếp theo play_count giảm dần
        List<Song> popularSongs = songRepository.findByStatusAndIsDeletedFalseOrderByPlayCountDesc(
                Song.SongStatus.PUBLISHED
        );

        List<Genre> genres = genreRepository.findByStatusAndIsDeletedFalse(Genre.GenreStatus.ACTIVE);

        List<Playlist> publicPlaylists = playlistRepository
                .findByVisibilityAndIsDeletedFalse(Playlist.PlaylistVisibility.PUBLIC);

        HomeDataResponse homeData = HomeDataResponse.builder()
                .banners(banners.stream().map(b -> HomeDataResponse.BannerItemDto.builder()
                        .id(b.getId())
                        .title(b.getTitle())
                        .subtitle(b.getSubtitle())
                        .ctaText(b.getCtaText())
                        .backgroundImageUrl(b.getBackgroundImageUrl())
                        .targetType(b.getTargetType())
                        .targetId(b.getTargetId())
                        .build()).collect(Collectors.toList()))
                .popularSongs(popularSongs.stream().map(s -> HomeDataResponse.SongDto.builder()
                        .id(s.getId())
                        .title(s.getTitle())
                        .slug(s.getSlug())
                        .durationSeconds(s.getDurationSeconds())
                        .audioUrl("/api/v1/songs/" + s.getId() + "/stream")
                        .coverImageUrl(s.getCoverImageUrl())
                        .playCount(s.getPlayCount())
                        .likeCount(s.getLikeCount())
                        .isVipOnly(s.getIsVipOnly())
                        .isDownloadable(s.getIsDownloadable())
                        .build()).collect(Collectors.toList()))
                .categories(genres.stream().map(g -> HomeDataResponse.CategoryDto.builder()
                        .id(g.getId())
                        .name(g.getName())
                        .slug(g.getSlug())
                        .coverImageUrl(g.getCoverImageUrl())
                        .build()).collect(Collectors.toList()))
                .recommendedPlaylists(publicPlaylists.stream().map(p -> HomeDataResponse.PlaylistDto.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .slug(p.getSlug())
                        .description(p.getDescription())
                        .coverImageUrl(p.getCoverImageUrl())
                        .totalSongs(p.getTotalSongs())
                        .totalDurationSeconds(p.getTotalDurationSeconds())
                        .build()).collect(Collectors.toList()))
                .build();

        return ResponseEntity.ok(ApiResponse.ok(homeData));
    }
}
