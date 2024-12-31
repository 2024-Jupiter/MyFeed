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
    Page<Post> findPagedPostsByUserId(long userId, Pageable pageable);

    // 게시글의 사용자 아이디 가져오기
    List<User> findUsersById(long userId);

    // 조회수 증가 (동시성)
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void updateViewCountById(@Param("id") Long id);

    // 좋아요 증가 (동시성)
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :id")
    void updateLikeCountById(@Param("id") Long id);

    // 좋아요 감소 (동시성)
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :id AND p.likeCount > 0")
    void decrementLikeCountById(@Param("id") Long id);

}