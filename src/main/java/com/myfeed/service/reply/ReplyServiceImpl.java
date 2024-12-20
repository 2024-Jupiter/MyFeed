package com.myfeed.service.reply;

import com.myfeed.model.reply.Reply;
import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostCommentList;
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
import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired
    ReplyRepository replyRepository;

    @Override
    public Reply findByCid(long cid) {
        return replyRepository.findById(cid).orElse(null);
    }

    @Override
    public List<Reply> getCommentsByPage(long pid, int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Post post = postRepository.findById(pid).orElse(null);
        Page<Reply> replyPage = replyRepository.findByPostContaining(post ,pageable);
        return replyPage.getContent();
    }

    @Override
    public Reply createComment(long uid, long pid, String content, LocalDateTime createAt, LocalDateTime updateAt) {
        User user = userRepository.findById(uid).orElse(null);
        Post post = postRepository.findById(pid).orElse(null);
        Reply reply = Reply.builder()
                .content(content).createAt(LocalDateTime.now()).updateAt(LocalDateTime.now())
                .build();
        PostCommentList postCommentList = PostCommentList.builder()
                .user(user).post(post).reply(reply)
                .build();
        reply.addPostCommentList(postCommentList);
        return replyRepository.save(reply);
    }

    @Override
    public void updateComment(Reply reply) {
        replyRepository.save(reply);
    }

    @Override
    public void deleteComment(long cid) {
        replyRepository.deleteById(cid);
    }

    @Override
    public List<Reply> getCommentsByUser(long uid) {
        return replyRepository.findByUserUid(uid);
    }
}
