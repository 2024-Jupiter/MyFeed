package com.myfeed.service.reply;

import com.myfeed.model.reply.Reply;
import com.myfeed.model.post.Post;
import com.myfeed.model.user.User;
import com.myfeed.repository.ReplyRepository;
import com.myfeed.repository.PostRepository;
import com.myfeed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired ReplyRepository replyRepository;

    @Override
    public Reply findByRid(long rid) {
        return replyRepository.findById(rid).orElse(null);
    }

    @Override
    public Reply createReply(long uid, long pid, String content) {
        User user = userRepository.findById(uid).orElse(null);
        Post post = postRepository.findById(pid).orElse(null);
        Reply reply = Reply.builder()
                .user(user).post(post).content(content)
                .createAt(LocalDateTime.now()).updateAt(LocalDateTime.now())
                .build();
        post.addReply(reply);
        return replyRepository.save(reply);
    }

    // 일레스틱 서치 게시글 내 댓글 리스트
    @Override
    public Page<Reply> getPageByPostContaining(int page, Post post) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return replyRepository.findByPostContaining(post, pageable);
    }

    @Override
    public void updateReply (Reply reply) {
        replyRepository.save(reply);
    }

    @Override
    public void deleteReply (long cid) {
        replyRepository.deleteById(cid);
    }
}
