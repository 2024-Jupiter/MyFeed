package com.myfeed.service.Post;

import com.myfeed.model.board.Tag;
import com.myfeed.model.post.Post;

import java.util.List;

public interface PostService {
    Post findByPid(long pid);

    Post createPost(long uid, Tag tag, String title, String content, String imgSrc);

    List<Post> getMyPostList(long uid);

    void updatePost(Post post);

    void deletePost(long pid);
}
