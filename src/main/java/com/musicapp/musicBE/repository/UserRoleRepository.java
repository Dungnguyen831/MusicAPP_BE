package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.UserRole;
import com.musicapp.musicBE.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
