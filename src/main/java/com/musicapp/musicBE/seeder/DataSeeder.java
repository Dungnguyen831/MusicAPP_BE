package com.musicapp.musicBE.seeder;

import com.musicapp.musicBE.entity.Artist;
import com.musicapp.musicBE.entity.BannerItem;
import com.musicapp.musicBE.entity.Song;
import com.musicapp.musicBE.repository.ArtistRepository;
import com.musicapp.musicBE.repository.BannerRepository;
import com.musicapp.musicBE.repository.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SongRepository songRepository;
    private final BannerRepository bannerRepository;
    private final ArtistRepository artistRepository;

    public DataSeeder(SongRepository songRepository, BannerRepository bannerRepository, ArtistRepository artistRepository) {
        this.songRepository = songRepository;
        this.bannerRepository = bannerRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Artists
        Artist kda = Artist.builder()
                .name("K/DA")
                .avatarUrl("https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=400&auto=format&fit=crop&q=60")
                .bio("K/DA is a virtual K-pop girl group consisting of League of Legends champions.")
                .build();

        Artist weeknd = Artist.builder()
                .name("The Weeknd")
                .avatarUrl("https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=400&auto=format&fit=crop&q=60")
                .bio("Abel Makkonen Tesfaye, known professionally as the Weeknd, is a Canadian singer-songwriter.")
                .build();

        Artist postMalone = Artist.builder()
                .name("Post Malone")
                .avatarUrl("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400&auto=format&fit=crop&q=60")
                .bio("Austin Richard Post, known professionally as Post Malone, is an American rapper, singer, and songwriter.")
                .build();

        artistRepository.saveAll(List.of(kda, weeknd, postMalone));

        // 2. Download sample MP3 for local stream demo if not exists
        downloadSampleMp3();

        // 3. Seed Songs
        // We use local stream URL: /api/v1/songs/{id}/stream
        Song s1 = Song.builder()
                .title("Aurora")
                .artistName("K/DA")
                .albumArtUrl("https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400&auto=format&fit=crop&q=60")
                .audioUrl("/api/v1/songs/1/stream")
                .duration(245)
                .genre("K-Pop")
                .isTrending(true)
                .streamCount(1524300L)
                .build();

        Song s2 = Song.builder()
                .title("The Baddest")
                .artistName("K/DA")
                .albumArtUrl("https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=400&auto=format&fit=crop&q=60")
                .audioUrl("/api/v1/songs/2/stream")
                .duration(180)
                .genre("K-Pop")
                .isTrending(true)
                .streamCount(2341900L)
                .build();

        Song s3 = Song.builder()
                .title("POP/STARS")
                .artistName("K/DA")
                .albumArtUrl("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400&auto=format&fit=crop&q=60")
                .audioUrl("/api/v1/songs/3/stream")
                .duration(191)
                .genre("K-Pop")
                .isTrending(true)
                .streamCount(4981200L)
                .build();

        Song s4 = Song.builder()
                .title("Circles Run")
                .artistName("Post Malone")
                .albumArtUrl("https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=400&auto=format&fit=crop&q=60")
                .audioUrl("/api/v1/songs/4/stream")
                .duration(215)
                .genre("Pop")
                .isTrending(false)
                .streamCount(872100L)
                .build();

        Song s5 = Song.builder()
                .title("Blinding Lights")
                .artistName("The Weeknd")
                .albumArtUrl("https://images.unsplash.com/photo-1511735111819-9a3f7709049c?w=400&auto=format&fit=crop&q=60")
                .audioUrl("/api/v1/songs/5/stream")
                .duration(200)
                .genre("Synthwave")
                .isTrending(true)
                .streamCount(9812300L)
                .build();

        songRepository.saveAll(List.of(s1, s2, s3, s4, s5));

        // 4. Seed Banners
        BannerItem b1 = BannerItem.builder()
                .title("Feel the Beat")
                .subtitle("Dive into the trending tracks of this week")
                .ctaText("Listen Now")
                .backgroundImageUrl("https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&auto=format&fit=crop&q=60")
                .targetType("song")
                .targetId(1L) // links to "Aurora"
                .build();

        BannerItem b2 = BannerItem.builder()
                .title("New Releases")
                .subtitle("Discover brand new albums and singles")
                .ctaText("Explore")
                .backgroundImageUrl("https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&auto=format&fit=crop&q=60")
                .targetType("song")
                .targetId(5L) // links to "Blinding Lights"
                .build();

        bannerRepository.saveAll(List.of(b1, b2));

        System.out.println(">>> Database Seeded Successfully!");
    }

    private void downloadSampleMp3() {
        String dirPath = "data/audio";
        String filePath = dirPath + "/sample.mp3";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println(">>> Downloading sample MP3 from SoundHelix to local storage...");
            String remoteUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
            try (BufferedInputStream in = new BufferedInputStream(URI.create(remoteUrl).toURL().openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                byte[] dataBuffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                System.out.println(">>> Download complete. Saved to: " + filePath);
            } catch (IOException e) {
                System.err.println(">>> Failed to download online sample MP3: " + e.getMessage());
                // Fallback: Create a tiny dummy file so the application doesn't crash
                try {
                    Files.write(Paths.get(filePath), new byte[1024]);
                    System.out.println(">>> Created dummy fallback MP3 file at: " + filePath);
                } catch (IOException ioException) {
                    System.err.println(">>> Critical error: Could not write dummy file: " + ioException.getMessage());
                }
            }
        }
    }
}
