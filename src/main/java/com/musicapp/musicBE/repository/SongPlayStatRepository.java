package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.SongPlayStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SongPlayStatRepository extends JpaRepository<SongPlayStat, Long> {
    Optional<SongPlayStat> findBySongIdAndStatDate(Long songId, LocalDate statDate);
}
