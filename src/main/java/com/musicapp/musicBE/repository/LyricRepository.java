package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.Lyric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LyricRepository extends JpaRepository<Lyric, Long> {
    Optional<Lyric> findBySongId(Long songId);
}
