package com.musicapp.musicBE.controller;

import com.musicapp.musicBE.dto.ApiResponse;
import com.musicapp.musicBE.dto.SongDetailDto;
import com.musicapp.musicBE.entity.Song;
import com.musicapp.musicBE.repository.SongRepository;
import com.musicapp.musicBE.service.SongCatalogService;
import com.musicapp.musicBE.service.SongStreamingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * REST controller for Song catalog and audio streaming.
 *
 * Implemented tasks
 *  - BE-3.2  GET /api/v1/songs            — published list with artists
 *  - BE-3.3  GET /api/v1/songs/{id}       — full detail (artists + genres + lyrics)
 *  - BE-3.4  GET /api/v1/songs/{id}/lyrics — lyrics only (synced LRC)
 *  - BE-4.1  GET /api/v1/songs/{id}/stream — byte-range audio streaming
 */
@Tag(name = "Song Management", description = "APIs quản lý bài hát và phát nhạc trực tuyến")
@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongRepository songRepository;
    private final SongStreamingService songStreamingService;
    private final SongCatalogService songCatalogService;

    public SongController(SongRepository songRepository,
                          SongStreamingService songStreamingService,
                          SongCatalogService songCatalogService) {
        this.songRepository = songRepository;
        this.songStreamingService = songStreamingService;
        this.songCatalogService = songCatalogService;
    }

    // -------------------------------------------------------------------------
    // BE-3.2 — Song list
    // -------------------------------------------------------------------------

    @Operation(
        summary = "Lấy danh sách bài hát",
        description = "Trả về tất cả bài hát PUBLISHED sắp xếp theo lượt nghe giảm dần. " +
                      "Mỗi bài hát đã bao gồm danh sách artists."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<SongDetailDto>>> getAllSongs() {
        List<SongDetailDto> songs = songCatalogService.getAllPublishedSongs();
        return ResponseEntity.ok(ApiResponse.ok(songs));
    }

    // -------------------------------------------------------------------------
    // BE-3.3 — Song detail (full: artists + genres + lyrics)
    // -------------------------------------------------------------------------

    @Operation(
        summary = "Chi tiết bài hát",
        description = "Trả về thông tin đầy đủ của một bài hát bao gồm: " +
                      "danh sách nghệ sĩ, thể loại và lời bài hát (có syncedLyrics LRC)."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SongDetailDto>> getSongById(
            @Parameter(description = "ID bài hát") @PathVariable Long id) {

        SongDetailDto dto = songCatalogService.getSongDetail(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    // -------------------------------------------------------------------------
    // BE-3.4 — Lyrics only (for SyncedLyricsViewer)
    // -------------------------------------------------------------------------

    @Operation(
        summary = "Lấy lời bài hát",
        description = "Trả về nội dung lời bài hát bao gồm `syncedLyrics` định dạng LRC " +
                      "để Flutter SyncedLyricsViewer cuộn đồng bộ theo thời gian phát nhạc."
    )
    @GetMapping("/{id}/lyrics")
    public ResponseEntity<ApiResponse<SongDetailDto.LyricDto>> getSongLyrics(
            @Parameter(description = "ID bài hát") @PathVariable Long id) {

        SongDetailDto.LyricDto lyric = songCatalogService.getSongLyrics(id);
        return ResponseEntity.ok(ApiResponse.ok(lyric));
    }

    // -------------------------------------------------------------------------
    // BE-4.1 — Audio streaming (HTTP 206 Partial Content)
    // -------------------------------------------------------------------------

    @Operation(
        summary = "Phát nhạc trực tuyến",
        description = "Hỗ trợ HTTP Range Request (206 Partial Content) để Flutter just_audio " +
                      "có thể seek/scrub mượt mà. " +
                      "Tự động tăng play_count khi bắt đầu phát (bytes=0-)."
    )
    @GetMapping("/{id}/stream")
    public ResponseEntity<ResourceRegion> streamSong(
            @Parameter(description = "ID bài hát") @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new IOException("Song not found with id: " + id));

        // Increment play count only on initial request (not on mid-scrub range requests)
        if (rangeHeader == null || rangeHeader.startsWith("bytes=0-")) {
            song.setPlayCount(song.getPlayCount() + 1);
            songRepository.save(song);
        }

        // TODO BE-4.2: Replace with per-song audio path resolution when cloud storage is added.
        String filePath = "data/audio/sample.mp3";
        return songStreamingService.streamAudio(filePath, rangeHeader);
    }
}
