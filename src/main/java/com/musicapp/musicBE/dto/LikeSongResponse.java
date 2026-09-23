package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeSongResponse {
    private Long songId;
    private Boolean liked;
    private Long likeCount;
}
