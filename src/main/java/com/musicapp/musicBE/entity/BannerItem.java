package com.musicapp.musicBE.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banner_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String subtitle;
    private String ctaText;
    private String backgroundImageUrl;
    private String targetType; // e.g. "song", "playlist", "url"
    private Long targetId;     // e.g. songId
}
