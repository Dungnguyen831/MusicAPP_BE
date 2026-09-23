package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.SongArtist;
import com.musicapp.musicBE.entity.SongArtistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongArtistRepository extends JpaRepository<SongArtist, SongArtistId> {
    List<SongArtist> findBySongId(Long songId);
}
