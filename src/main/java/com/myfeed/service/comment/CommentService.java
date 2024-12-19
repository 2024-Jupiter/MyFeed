package com.myfeed.service.comment;

import com.myfeed.model.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    public static final int PAGE_SIZE = 10;

    Comment findByCid(long cid);

    List<Comment> getCommentsByPage(long pid, int page);

    Comment createComment(String uid, long pid, String content, LocalDateTime createAt, LocalDateTime updateAt);

    void updateComment(Comment comment);

    void deleteComment(long cid);

    List<Comment> getCommentsByUser(String uid);
}
