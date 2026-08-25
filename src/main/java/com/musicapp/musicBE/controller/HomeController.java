package com.musicapp.musicBE.controller;

import com.musicapp.musicBE.dto.HomeDataResponse;
import com.musicapp.musicBE.entity.BannerItem;
import com.musicapp.musicBE.entity.Song;
import com.musicapp.musicBE.repository.BannerRepository;
import com.musicapp.musicBE.repository.SongRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    private final BannerRepository bannerRepository;
    private final SongRepository songRepository;

    public HomeController(BannerRepository bannerRepository, SongRepository songRepository) {
        this.bannerRepository = bannerRepository;
        this.songRepository = songRepository;
    }

    @GetMapping("/data")
    public ResponseEntity<HomeDataResponse> getHomeData() {
        List<BannerItem> banners = bannerRepository.findAll();
        List<Song> popularSongs = songRepository.findByIsTrendingTrue();
        
        // Dynamically extract categories from distinct genres
        List<String> categories = songRepository.findAll().stream()
                .map(Song::getGenre)
                .distinct()
                .collect(Collectors.toList());

        if (categories.isEmpty()) {
            categories = List.of("K-Pop", "Pop", "Synthwave", "R&B");
        }

        HomeDataResponse response = HomeDataResponse.builder()
                .banners(banners)
                .popularSongs(popularSongs)
                .categories(categories)
                .build();

        return ResponseEntity.ok(response);
    }
}
