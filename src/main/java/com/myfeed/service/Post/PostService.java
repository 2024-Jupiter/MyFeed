package com.myfeed.service.Post;

import com.myfeed.model.post.Category;
import com.myfeed.model.post.Post;
import org.springframework.data.domain.Page;

public interface PostService {
    public static final int PAGE_SIZE = 10;

    Post findByPid(long pid);

    Post createPost(long uid, Category category, String title, String content, String imgSrc);

    Page<Post> getMyPostList(int page, long uid);

    void updatePost(Post post);

    void deletePost(long pid);

    void incrementViewCount(long pid);

    void incrementLikeCount(long pid);

    void decrementLikeCount(long pid);
}
