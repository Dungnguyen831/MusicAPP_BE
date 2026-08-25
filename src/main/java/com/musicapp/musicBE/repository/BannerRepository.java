package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.BannerItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<BannerItem, Long> {
}
