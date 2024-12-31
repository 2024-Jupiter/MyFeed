package com.myfeed.service.reply;

import com.myfeed.model.reply.Reply;
import com.myfeed.model.post.Post;
import com.myfeed.model.user.User;
import com.myfeed.repository.jpa.ReplyRepository;
import com.myfeed.repository.jpa.PostRepository;
import com.myfeed.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired ReplyRepository replyRepository;

    // 댓글 가져오기
    @Override
    public Reply findByRid(long rid) {
        return replyRepository.findById(rid).orElse(null);
    }

    // 댓글 작성
    @Override
    public Reply createReply(long uid, long pid, String content) {
        User user = userRepository.findById(uid).orElse(null);
        Post post = postRepository.findById(pid).orElse(null);
        Reply reply = Reply.builder()
                .user(user).post(post).content(content)
                .build();
        post.addReply(reply);
        return replyRepository.save(reply);
    }

    // 게시글 내 댓글 리스트
    @Override
    public Page<Reply> getPageByPostContaining(int page, Post post) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return replyRepository.findByPostContaining(post, pageable);
    }

    // 댓글 수정
    @Override
    public void updateReply (Reply reply) {
        replyRepository.save(reply);
    }

    // 댓글 삭제
    @Override
    public void deleteReply (long cid) {
         replyRepository.deleteById(cid);
    }
}
