package com.musicapp.musicBE.dto;

import lombok.Data;

@Data
public class CreatePlaylistRequest {
    private String title;
    private String description;
    private String visibility;
}
