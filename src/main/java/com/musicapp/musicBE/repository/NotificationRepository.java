package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTargetTypeAndTargetId(Notification.NotificationTargetType targetType, Long targetId);
}
