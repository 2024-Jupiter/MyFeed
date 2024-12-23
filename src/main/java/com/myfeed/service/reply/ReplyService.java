package com.myfeed.service.reply;

import com.myfeed.model.reply.Reply;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyService {
    public static final int PAGE_SIZE = 10;

    Reply findByRid(long rid);

    Reply createReply(long uid, long pid, String content);

    void updateReply(Reply reply);

    void deleteReply(long cid);
}
