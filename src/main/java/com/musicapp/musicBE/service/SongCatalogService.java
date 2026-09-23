package com.musicapp.musicBE.service;

import com.musicapp.musicBE.dto.SongDetailDto;
import com.musicapp.musicBE.entity.*;
import com.musicapp.musicBE.repository.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BE-3.3: Service layer for Song catalog operations.
 * Assembles SongDetailDto with artist, genre, and lyric joins.
 */
@Service
public class SongCatalogService {

    private final SongRepository songRepository;
    private final SongArtistRepository songArtistRepository;
    private final ArtistRepository artistRepository;
    private final SongGenreRepository songGenreRepository;
    private final GenreRepository genreRepository;
    private final LyricRepository lyricRepository;

    public SongCatalogService(SongRepository songRepository,
                               SongArtistRepository songArtistRepository,
                               ArtistRepository artistRepository,
                               SongGenreRepository songGenreRepository,
                               GenreRepository genreRepository,
                               LyricRepository lyricRepository) {
        this.songRepository = songRepository;
        this.songArtistRepository = songArtistRepository;
        this.artistRepository = artistRepository;
        this.songGenreRepository = songGenreRepository;
        this.genreRepository = genreRepository;
        this.lyricRepository = lyricRepository;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Returns a list of all PUBLISHED songs with artists populated.
     * Genres and lyrics are NOT loaded here to keep the list response lean.
     */
    public List<SongDetailDto> getAllPublishedSongs() {
        return songRepository
                .findByStatusAndIsDeletedFalseOrderByPlayCountDesc(Song.SongStatus.PUBLISHED)
                .stream()
                .map(song -> toDto(song, /* includeGenres */ false, /* includeLyrics */ false))
                .collect(Collectors.toList());
    }

    /**
     * Returns the full detail of a single song including artists, genres, and lyrics.
     * Throws 404 if the song does not exist or is deleted/unpublished.
     */
    public SongDetailDto getSongDetail(Long id) {
        Song song = songRepository.findById(id)
                .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                .filter(s -> s.getStatus() == Song.SongStatus.PUBLISHED)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Song not found with id: " + id));

        return toDto(song, /* includeGenres */ true, /* includeLyrics */ true);
    }

    /**
     * Returns only the lyrics for a song.
     * Throws 404 if the song or its lyrics do not exist.
     */
    public SongDetailDto.LyricDto getSongLyrics(Long id) {
        // Ensure the song exists
        songRepository.findById(id)
                .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Song not found with id: " + id));

        return lyricRepository.findBySongId(id)
                .map(l -> SongDetailDto.LyricDto.builder()
                        .id(l.getId())
                        .content(l.getContent())
                        .syncedLyrics(l.getSyncedLyrics())
                        .language(l.getLanguage())
                        .build())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lyrics not found for song id: " + id));
    }

    // -------------------------------------------------------------------------
    // Mapping helpers
    // -------------------------------------------------------------------------

    /**
     * Maps a Song entity to SongDetailDto.
     * Artist join is always performed; genre and lyric joins are conditional.
     */
    public SongDetailDto toDto(Song song, boolean includeGenres, boolean includeLyrics) {
        // --- Artists (always included) ---
        List<SongDetailDto.ArtistDto> artists = songArtistRepository
                .findBySongId(song.getId())
                .stream()
                .map(sa -> artistRepository.findById(sa.getArtistId()).orElse(null))
                .filter(artist -> artist != null && !Boolean.TRUE.equals(artist.getIsDeleted()))
                .map(a -> SongDetailDto.ArtistDto.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .slug(a.getSlug())
                        .avatarUrl(a.getAvatarUrl())
                        .build())
                .collect(Collectors.toList());

        // --- Genres (conditional) ---
        List<SongDetailDto.GenreDto> genres = null;
        if (includeGenres) {
            genres = songGenreRepository
                    .findBySongId(song.getId())
                    .stream()
                    .map(sg -> genreRepository.findById(sg.getGenreId()).orElse(null))
                    .filter(g -> g != null && !Boolean.TRUE.equals(g.getIsDeleted()))
                    .map(g -> SongDetailDto.GenreDto.builder()
                            .id(g.getId())
                            .name(g.getName())
                            .slug(g.getSlug())
                            .build())
                    .collect(Collectors.toList());
        }

        // --- Lyrics (conditional) ---
        SongDetailDto.LyricDto lyricDto = null;
        if (includeLyrics) {
            lyricDto = lyricRepository.findBySongId(song.getId())
                    .map(l -> SongDetailDto.LyricDto.builder()
                            .id(l.getId())
                            .content(l.getContent())
                            .syncedLyrics(l.getSyncedLyrics())
                            .language(l.getLanguage())
                            .build())
                    .orElse(null);
        }

        return SongDetailDto.builder()
                .id(song.getId())
                .title(song.getTitle())
                .slug(song.getSlug())
                .durationSeconds(song.getDurationSeconds())
                .audioUrl("/api/v1/songs/" + song.getId() + "/stream")
                .coverImageUrl(song.getCoverImageUrl())
                .fileSizeBytes(song.getFileSizeBytes())
                .mimeType(song.getMimeType())
                .bitrate(song.getBitrate())
                .releaseDate(song.getReleaseDate())
                .playCount(song.getPlayCount())
                .likeCount(song.getLikeCount())
                .isVipOnly(song.getIsVipOnly())
                .isDownloadable(song.getIsDownloadable())
                .artists(artists)
                .genres(genres)
                .lyrics(lyricDto)
                .build();
    }
}
