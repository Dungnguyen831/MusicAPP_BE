package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddSongToPlaylistResponse {
    private Long playlistId;
    private Long songId;
    private Integer position;
    private Integer totalSongs;
    private Long totalDurationSeconds;
}
