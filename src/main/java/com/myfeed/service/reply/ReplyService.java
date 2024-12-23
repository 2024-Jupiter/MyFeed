package com.myfeed.service.reply;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyService {
    public static final int PAGE_SIZE = 10;

    Reply findByRid(long rid);

    Reply createReply(long uid, long pid, String content);

    Page<Reply> getPageByPostContaining(int page, Post post);

    void updateReply(Reply reply);

    void deleteReply(long cid);
}
