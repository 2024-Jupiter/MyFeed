package com.myfeed.repository;

import com.myfeed.model.comment.Comment;
import com.myfeed.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserUid(String uid);

    Page<Comment> findByPostContaining(Post post, Pageable pageable);
}
