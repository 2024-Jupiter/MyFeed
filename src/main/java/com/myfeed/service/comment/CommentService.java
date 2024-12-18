package com.myfeed.service.comment;

import com.myfeed.model.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    Comment createComment(String uid);

    void updateComment(Comment comment);

    void deleteComment(long cid);

    List<Comment> getCommentsByUser(String uid);

    List<Comment> getCommentsByDateRange(LocalDateTime start, LocalDateTime end);
}
