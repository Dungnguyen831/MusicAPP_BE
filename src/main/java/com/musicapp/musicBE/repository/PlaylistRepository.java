package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserIdAndIsDeletedFalse(Long userId);
    List<Playlist> findByVisibilityAndIsDeletedFalse(Playlist.PlaylistVisibility visibility);
    Optional<Playlist> findBySlugAndIsDeletedFalse(String slug);
}
