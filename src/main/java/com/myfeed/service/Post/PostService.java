package com.myfeed.service.Post;

import com.myfeed.model.Post.Post;
import com.myfeed.model.Post.PostCommentList;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    Post createPost(String uid, List<PostCommentList> postCommentList, String category,
                    String title, String content, String imgSrc, LocalDateTime createAt,
                    LocalDateTime updateAt, int viewCount, int likeCount);

    void updatePost(Post post);

    void deletePost(long pid);

    List<Post> getPostsByUser(String uid);

    List<Post> getPostsByDateRange(LocalDateTime start, LocalDateTime end);
}
