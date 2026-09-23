package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.ArtistPlayStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ArtistPlayStatRepository extends JpaRepository<ArtistPlayStat, Long> {
    Optional<ArtistPlayStat> findByArtistIdAndStatDate(Long artistId, LocalDate statDate);
}
