package com.myfeed.service.Post;

import com.myfeed.model.post.Category;
import com.myfeed.model.post.Post;
import com.myfeed.model.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    public static final int PAGE_SIZE = 10;

    // 게시글 가져오기
    Post findPostById(long id);

    // 내 게시글 페이지네이션
    Page<Post> getPagedPostsByUserId(int page, long userId);

    // 게시글의 사용자 아이디 가져오기
    List<User> getUsersById(long userId);

    // 게시글 생성 또는 업데이트
    Post createOrUpdatePost(Post post);

    // 게시글 삭제
    void deletePostById(long id);

    // 조회수 증가 (동시성)
    void incrementPostViewCountById(long id);

    // 좋아요 증가 (동시성)
    void incrementPostLikeCountById(long id);

    // 좋아요 감소 (동시성)
    void decrementPostLikeCountById(long id);

    /*
    // 게시글 작성
    Post createPost(long userId, Category category, String title, String content, String imgSrc);

    // 게시글 수정
    void updatePost(Post post);

    // 조회수 증가
    Post incrementPostViewCountById(long id);

    // 좋아요 증가
    Post incrementPostLikeCountById(long id);

    // 좋아요 감소
    Post decrementPostLikeCountById(long id);
     */
}
