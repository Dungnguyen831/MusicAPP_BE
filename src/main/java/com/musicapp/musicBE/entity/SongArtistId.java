package com.musicapp.musicBE.entity;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongArtistId implements Serializable {
    private Long songId;
    private Long artistId;
    private SongArtist.ArtistRole role;
}
