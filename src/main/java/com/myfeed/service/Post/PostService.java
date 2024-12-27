package com.myfeed.service.Post;

import com.myfeed.model.post.Category;
import com.myfeed.model.post.Post;
import org.springframework.data.domain.Page;

public interface PostService {
    public static final int PAGE_SIZE = 10;

    // 게시글 가져오기
    Post findByPid(long pid);

    // 게시글 작성
    Post createPost(long uid, Category category, String title, String content, String imgSrc);

    // 내 게시글 페이지네이션
    Page<Post> getMyPostList(int page, long uid);

    // 게시글 수정
    void updatePost(Post post);

    // 게시글 삭제
    void deletePost(long pid);

    // 조회수 증가
    void incrementViewCount(long pid);

    // 좋아요 증가
    void incrementLikeCount(long pid);

    // 좋아요 감소
    void decrementLikeCount(long pid);
}
