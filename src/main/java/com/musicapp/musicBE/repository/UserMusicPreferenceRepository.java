package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.UserMusicPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserMusicPreferenceRepository extends JpaRepository<UserMusicPreference, Long> {
    List<UserMusicPreference> findByUserIdOrderByScoreDesc(Long userId);
}
