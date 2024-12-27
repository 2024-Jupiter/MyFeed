package com.myfeed.repository;

import com.myfeed.model.post.Post;
import com.myfeed.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 내 게시글 페이지네이션
    Page<Post> findByUserUid(long uid, Pageable pageable);

    User findByUserUid(long uid);

    // 조회수 증가
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.pid = :pid")
    void incrementViewCount(@Param("pid") Long pid);

    // 좋아요 증가
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.pid = :pid")
    void incrementLikeCount(@Param("pid") Long pid);

    // 좋아요 감소
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.pid = :pid AND p.likeCount > 0")
    void decrementLikeCount(@Param("pid") Long pid);
}