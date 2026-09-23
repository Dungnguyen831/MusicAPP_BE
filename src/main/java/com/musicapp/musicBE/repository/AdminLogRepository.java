package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {
    List<AdminLog> findByAdminIdOrderByCreatedAtDesc(Long adminId);
}
