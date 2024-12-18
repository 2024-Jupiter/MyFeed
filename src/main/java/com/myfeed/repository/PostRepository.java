package com.myfeed.repository;

import com.myfeed.model.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserUid(String uid);

    // 게시글 삭제
    List<Post> findByPostDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
