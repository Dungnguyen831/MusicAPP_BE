package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.RecommendationScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecommendationScoreRepository extends JpaRepository<RecommendationScore, Long> {
    List<RecommendationScore> findByUserIdOrderByScoreDesc(Long userId);
}
