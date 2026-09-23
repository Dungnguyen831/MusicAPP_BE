package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<UserNotification> findByUserIdAndNotificationId(Long userId, Long notificationId);
}
