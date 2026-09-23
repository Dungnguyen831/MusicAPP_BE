package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findBySlug(String slug);
    List<Genre> findByStatusAndIsDeletedFalse(Genre.GenreStatus status);
}
