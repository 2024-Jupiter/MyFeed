package com.myfeed.service.Post;

import com.myfeed.model.post.Post;

import java.util.List;

public interface PostService {
    Post findByPid(long pid);

    Post createPost(String uid, String category, String title, String content, String imgSrc);

    void updatePost(Post post);

    void deletePost(long pid);

    List<Post> getPostsByUser(String uid);
}
