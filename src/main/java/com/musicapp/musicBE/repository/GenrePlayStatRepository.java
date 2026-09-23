package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.GenrePlayStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GenrePlayStatRepository extends JpaRepository<GenrePlayStat, Long> {
    Optional<GenrePlayStat> findByGenreIdAndStatDate(Long genreId, LocalDate statDate);
}
