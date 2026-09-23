package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.SongGenre;
import com.musicapp.musicBE.entity.SongGenreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongGenreRepository extends JpaRepository<SongGenre, SongGenreId> {
    List<SongGenre> findBySongId(Long songId);
}
