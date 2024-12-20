package com.myfeed.service.Post;

import com.myfeed.model.post.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    Post createPost(String uid, String category, String title, String content, String imgSrc);

    void updatePost(Post post);

    void deletePost(long pid);

    List<Post> getPostsByUser(String uid);
}
