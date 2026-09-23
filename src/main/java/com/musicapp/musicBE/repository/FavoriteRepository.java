package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserIdAndTargetType(Long userId, Favorite.FavoriteType targetType);
    Optional<Favorite> findByUserIdAndTargetTypeAndTargetId(Long userId, Favorite.FavoriteType targetType, Long targetId);
}
