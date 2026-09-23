package com.musicapp.musicBE.seeder;

import com.musicapp.musicBE.entity.*;
import com.musicapp.musicBE.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SongRepository songRepository;
    private final BannerRepository bannerRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public DataSeeder(SongRepository songRepository, BannerRepository bannerRepository, 
                      ArtistRepository artistRepository, GenreRepository genreRepository,
                      PlaylistRepository playlistRepository, PlaylistSongRepository playlistSongRepository,
                      UserRepository userRepository, RoleRepository roleRepository,
                      UserRoleRepository userRoleRepository) {
        this.songRepository = songRepository;
        this.bannerRepository = bannerRepository;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
        this.playlistRepository = playlistRepository;
        this.playlistSongRepository = playlistSongRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        downloadSampleMp3();

        if (userRepository.count() > 0) return;

        Role userRole = roleRepository.save(Role.builder().name("ROLE_USER").description("User").build());
        Role adminRole = roleRepository.save(Role.builder().name("ROLE_ADMIN").description("Admin").build());

        User admin = userRepository.save(User.builder()
                .email("admin@musicapp.com").username("admin").passwordHash("hashed_pwd")
                .fullName("System Admin").status(User.UserStatus.ACTIVE).emailVerified(true).build());

        User demoUser = userRepository.save(User.builder()
                .email("user@musicapp.com").username("demo_user").passwordHash("hashed_pwd")
                .fullName("Demo User").status(User.UserStatus.ACTIVE).emailVerified(true).build());

        userRoleRepository.save(new UserRole(admin.getId(), adminRole.getId(), LocalDateTime.now()));
        userRoleRepository.save(new UserRole(demoUser.getId(), userRole.getId(), LocalDateTime.now()));

        Genre pop = genreRepository.save(Genre.builder().name("Pop").slug("pop").description("Pop").build());
        Genre edm = genreRepository.save(Genre.builder().name("EDM").slug("edm").description("EDM").build());

        artistRepository.save(Artist.builder().name("K/DA").slug("kda").avatarUrl("https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17").status(Artist.ArtistStatus.ACTIVE).build());

        downloadSampleMp3();

        Song s1 = songRepository.save(Song.builder().title("Demo Song 1").slug("demo-song-1")
                .durationSeconds(210).audioUrl("/api/v1/songs/1/stream")
                .coverImageUrl("https://images.unsplash.com/photo-1514525253161-7a46d19cd819")
                .status(Song.SongStatus.PUBLISHED).playCount(1500L).likeCount(120L).build());

        Song s2 = songRepository.save(Song.builder().title("Blinding Lights").slug("blinding-lights")
                .durationSeconds(200).audioUrl("/api/v1/songs/2/stream")
                .coverImageUrl("https://images.unsplash.com/photo-1511735111819-9a3f7709049c")
                .status(Song.SongStatus.PUBLISHED).playCount(98123L).likeCount(5420L).build());

        bannerRepository.save(BannerItem.builder().title("Feel the Beat").subtitle("Trending tracks")
                .ctaText("Listen Now").backgroundImageUrl("https://images.unsplash.com/photo-1514525253161-7a46d19cd819")
                .targetType("song").targetId(s1.getId()).build());

        Playlist topTrending = playlistRepository.save(Playlist.builder().title("Top Trending").slug("top-trending")
                .description("Popular tracks").visibility(Playlist.PlaylistVisibility.PUBLIC)
                .playlistType(Playlist.PlaylistType.SYSTEM).totalSongs(2).totalDurationSeconds(410L).build());

        playlistSongRepository.save(PlaylistSong.builder().playlistId(topTrending.getId()).songId(s1.getId()).position(1).build());
        playlistSongRepository.save(PlaylistSong.builder().playlistId(topTrending.getId()).songId(s2.getId()).position(2).build());

        System.out.println(">>> Database Seeded Successfully!");
    }

    private void downloadSampleMp3() {
        String dirPath = "data/audio";
        String filePath = dirPath + "/sample.mp3";
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(filePath);
        if (!file.exists()) {
            String remoteUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
            try (BufferedInputStream in = new BufferedInputStream(URI.create(remoteUrl).toURL().openStream());
                 FileOutputStream fos = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[4096];
                int len;
                while ((len = in.read(buffer, 0, 1024)) != -1) {
                    fos.write(buffer, 0, len);
                }
            } catch (IOException e) {
                System.err.println(">>> Failed to download MP3 sample: " + e.getMessage());
            }
        }
    }
}
