package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserPlaylistResponse {
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
