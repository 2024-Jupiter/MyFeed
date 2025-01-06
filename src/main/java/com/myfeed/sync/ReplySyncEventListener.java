package com.myfeed.sync;

import com.myfeed.exception.ExpectedException;
import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostEs;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.reply.ReplyDetailDto;
import com.myfeed.model.user.User;
import com.myfeed.repository.elasticsearch.PostEsRepository;
import com.myfeed.repository.jpa.PostRepository;
import com.myfeed.repository.jpa.ReplyRepository;
import com.myfeed.repository.jpa.UserRepository;
import com.myfeed.response.ErrorCode;
import com.myfeed.service.Post.PostEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReplySyncEventListener {
    @Autowired
    private PostEsRepository postEsRepository;
    @Autowired private PostEsService postEsService;
    @Autowired private ReplyRepository replyRepository;

    // 댓글 작성
    @Async
    //@EventListener
    @TransactionalEventListener
    public void handleReplySyncEvent(ReplySyncEvent event) {
        if ("CREATE_OR_UPDATE".equals(event.getOperation())) {
            Reply reply = replyRepository.findById(event.getReplyId()).orElseThrow(() -> new ExpectedException(ErrorCode.REPLY_NOT_FOUND));

            PostEs postEs = postEsRepository.findById(String.valueOf(reply.getPost().getId()))
                    .orElseThrow(() -> new ExpectedException(ErrorCode.POST_NOT_FOUND));

            List<Map<String, Object>> replies = replyRepository.findByPostId(reply.getPost().getId()).stream()
                    .map(replyItem -> {
                        Map<String, Object> replyMap = new HashMap<>();
                        replyMap.put("nickname", replyItem.getUser().getNickname());
                        replyMap.put("content", replyItem.getContent());
                        replyMap.put("date", replyItem.getCreatedAt().isBefore(replyItem.getUpdatedAt())
                                ? replyItem.getCreatedAt() : replyItem.getUpdatedAt());
                        return replyMap;
                    })
                    .collect(Collectors.toList());

            postEs.setReplies(replies);

            postEsService.updateToElasticsearch(postEs);
        } else if ("DELETE".equals(event.getOperation())) {
            PostEs postEs = postEsRepository.findById(String.valueOf(event.getReplyId()))
                    .orElseThrow(() -> new ExpectedException(ErrorCode.POST_NOT_FOUND));
            postEsService.deleteToElasticsearch(postEs, String.valueOf(event.getReplyId()));
        }
    }
}
