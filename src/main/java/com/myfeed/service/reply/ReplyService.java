package com.myfeed.service.reply;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyService {
    public static final int PAGE_SIZE = 10;

    // 댓글 가져오기
    Reply findByReplyId(long id);

    // 댓글 작성
    Reply createReply(long uid, long pid, String content);

    // 게시글 내 댓글 페이지네이션
    Page<Reply> getPagedRepliesByPost(int page, Post post);

    // 댓글 수정
    void updateReply(Reply reply);

    // 댓글 삭제
    void deleteReply(long id);
}
