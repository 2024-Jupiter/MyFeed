package com.myfeed.repository;

import com.myfeed.model.comment.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SplittableRandom;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserUid(String uid);

    List<Comment> findByPostContaining(long pid, Pageable pageable);

    // 게시글 삭제
    List<Comment> findByCommentDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
