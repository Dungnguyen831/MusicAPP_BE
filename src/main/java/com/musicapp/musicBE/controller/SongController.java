package com.musicapp.musicBE.controller;

import com.musicapp.musicBE.entity.Song;
import com.musicapp.musicBE.repository.SongRepository;
import com.musicapp.musicBE.service.SongStreamingService;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongRepository songRepository;
    private final SongStreamingService songStreamingService;

    public SongController(SongRepository songRepository, SongStreamingService songStreamingService) {
        this.songRepository = songRepository;
        this.songStreamingService = songStreamingService;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(songRepository.findAll());
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<ResourceRegion> streamSong(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        
        // Optionally update stream count on stream start
        if (rangeHeader == null || rangeHeader.startsWith("bytes=0-")) {
            songRepository.findById(id).ifPresent(song -> {
                song.setStreamCount(song.getStreamCount() + 1);
                songRepository.save(song);
            });
        }

        return songStreamingService.streamAudio(rangeHeader);
    }
}
