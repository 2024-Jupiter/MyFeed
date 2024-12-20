package com.myfeed.service.reply;

import com.myfeed.model.reply.Reply;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyService {
    public static final int PAGE_SIZE = 10;

    Reply findByCid(long cid);

    List<Reply> getCommentsByPage(long pid, int page);

    Reply createComment(long uid, long pid, String content, LocalDateTime createAt, LocalDateTime updateAt);

    void updateComment(Reply reply);

    void deleteComment(long cid);

    List<Reply> getCommentsByUser(long uid);
}
