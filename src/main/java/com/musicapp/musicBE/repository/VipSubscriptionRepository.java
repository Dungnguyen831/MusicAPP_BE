package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.VipSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VipSubscriptionRepository extends JpaRepository<VipSubscription, Long> {
    List<VipSubscription> findByUserIdAndStatus(Long userId, VipSubscription.SubscriptionStatus status);
}
