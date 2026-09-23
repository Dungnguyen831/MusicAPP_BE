package com.musicapp.musicBE.controller;

import com.musicapp.musicBE.dto.SongDetailDto;
import com.musicapp.musicBE.entity.Song;
import com.musicapp.musicBE.repository.SongRepository;
import com.musicapp.musicBE.service.SongStreamingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Song Management", description = "APIs quản lý bài hát và phát nhạc trực tuyến")
@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongRepository songRepository;
    private final SongStreamingService songStreamingService;

    public SongController(SongRepository songRepository, SongStreamingService songStreamingService) {
        this.songRepository = songRepository;
        this.songStreamingService = songStreamingService;
    }

    @Operation(summary = "Lấy danh sách bài hát", description = "Trả về tất cả bài hát đã xuất bản (PUBLISHED)")
    @GetMapping
    public ResponseEntity<List<SongDetailDto>> getAllSongs() {
        List<Song> songs = songRepository.findByStatus(Song.SongStatus.PUBLISHED);
        List<SongDetailDto> dtos = songs.stream().map(s -> SongDetailDto.builder()
                .id(s.getId())
                .title(s.getTitle())
                .slug(s.getSlug())
                .durationSeconds(s.getDurationSeconds())
                .audioUrl(s.getAudioUrl())
                .coverImageUrl(s.getCoverImageUrl())
                .playCount(s.getPlayCount())
                .likeCount(s.getLikeCount())
                .isVipOnly(s.getIsVipOnly())
                .isDownloadable(s.getIsDownloadable())
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<ResourceRegion> streamSong(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new IOException("Song not found"));

        // Update play count on stream start
        if (rangeHeader == null || rangeHeader.startsWith("bytes=0-")) {
            song.setPlayCount(song.getPlayCount() + 1);
            songRepository.save(song);
        }

        // In a real app, audioUrl might be a relative path on disk or a cloud URL.
        // For this demo, we assume the file is named based on id or we have a sample.
        String filePath = "data/audio/sample.mp3"; 
        return songStreamingService.streamAudio(filePath, rangeHeader);
    }
}

