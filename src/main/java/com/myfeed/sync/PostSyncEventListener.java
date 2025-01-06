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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostSyncEventListener {
    @Autowired private PostEsService postEsService;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    // 게시글 작성
    @Async
    //@EventListener
    @TransactionalEventListener
    public void handlePostSyncEvent(PostSyncEvent event) {
        if ("CREATE_OR_UPDATE".equals(event.getOperation())) {
            Post post = postRepository.findById(event.getPostId()).orElseThrow(() -> new ExpectedException(ErrorCode.POST_NOT_FOUND));
            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new ExpectedException(ErrorCode.USER_NOT_FOUND));

            PostEs postEs = new PostEs();
            postEs.setId(String.valueOf(post.getId())); // id 중복 문제 해결
            postEs.setNickname(user.getNickname());
            postEs.setTitle(post.getTitle());
            postEs.setContent(post.getContent());
            postEs.setCategory(post.getCategory());
            postEs.setViewCount(post.getViewCount());
            postEs.setLikeCount(post.getLikeCount());
            //postEs.setReplies(new ArrayList<>());

            postEsService.syncToElasticsearch(postEs);
        } else if ("DELETE".equals(event.getOperation())) {
            postEsService.deleteFromElasticsearch(String.valueOf(event.getPostId()));
        }
    }
}