package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.ListeningHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {
    List<ListeningHistory> findByUserIdOrderByListenedAtDesc(Long userId);
}
