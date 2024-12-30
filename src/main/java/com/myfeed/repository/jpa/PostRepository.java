package com.myfeed.repository.jpa;

import com.myfeed.model.post.Post;
import com.myfeed.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 내 게시글 페이지네이션
    Page<Post> findByUserId(long uid, Pageable pageable);
    List<User> findByUserUid(long uid);

    // 조회수 증가
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :pid")
    void incrementViewCount(@Param("pid") Long pid);

    // 좋아요 증가
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :pid")
    void incrementLikeCount(@Param("pid") Long pid);

    // 좋아요 감소
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :pid AND p.likeCount > 0")
    void decrementLikeCount(@Param("pid") Long pid);
}