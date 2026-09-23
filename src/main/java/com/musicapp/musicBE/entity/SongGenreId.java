package com.musicapp.musicBE.entity;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongGenreId implements Serializable {
    private Long songId;
    private Long genreId;
}
