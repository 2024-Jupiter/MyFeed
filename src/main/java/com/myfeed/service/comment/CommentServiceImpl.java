package com.myfeed.service.comment;

import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostCommentList;
import com.myfeed.model.comment.Comment;
import com.myfeed.model.user.User;
import com.myfeed.repository.CommentRepository;
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
public class CommentServiceImpl implements CommentService {
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;

    @Override
    public Comment findByCid(long cid) {
        return commentRepository.findById(cid).orElse(null);
    }

    @Override
    public List<Comment> getCommentsByPage(long pid, int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Post post = postRepository.findById(pid).orElse(null);
        Page<Comment> commentPage = commentRepository.findByPostContaining(post ,pageable);
        return commentPage.getContent();
    }

    @Override
    public Comment createComment(String uid, long pid, String content, LocalDateTime createAt, LocalDateTime updateAt) {
        User user = userRepository.findById(uid).orElse(null);
        Post post = postRepository.findById(pid).orElse(null);
        Comment comment = Comment.builder()
                .content(content).createAt(LocalDateTime.now()).updateAt(LocalDateTime.now())
                .build();
        PostCommentList postCommentList = PostCommentList.builder()
                .user(user).post(post).comment(comment)
                .build();
        comment.addPostCommentList(postCommentList);
        return commentRepository.save(comment);
    }

    @Override
    public void updateComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(long cid) {
        commentRepository.deleteById(cid);
    }

    @Override
    public List<Comment> getCommentsByUser(String uid) {
        return commentRepository.findByUserUid(uid);
    }
}
