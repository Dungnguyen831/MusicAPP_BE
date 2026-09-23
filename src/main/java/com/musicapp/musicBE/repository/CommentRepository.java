package com.musicapp.musicBE.repository;

import com.musicapp.musicBE.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySongIdAndStatusOrderByCreatedAtDesc(Long songId, Comment.CommentStatus status);
}
