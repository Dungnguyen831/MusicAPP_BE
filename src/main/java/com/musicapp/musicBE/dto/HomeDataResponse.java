package com.musicapp.musicBE.dto;

import com.musicapp.musicBE.entity.BannerItem;
import com.musicapp.musicBE.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDataResponse {
    private List<BannerItem> banners;
    private List<Song> popularSongs;
    private List<String> categories;
}
