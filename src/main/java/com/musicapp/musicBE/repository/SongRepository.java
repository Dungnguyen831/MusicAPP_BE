package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    java.util.Optional<Song> findBySlug(String slug);
    List<Song> findByStatus(Song.SongStatus status);
    List<Song> findByStatusAndIsDeletedFalseOrderByPlayCountDesc(Song.SongStatus status);
}
