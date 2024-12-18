package com.myfeed.service.comment;

import com.myfeed.model.comment.Comment;
import com.myfeed.model.user.User;
import com.myfeed.repository.CommentRepository;
import com.myfeed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired UserRepository userRepository;
    @Autowired CommentRepository commentRepository;

    @Override
    public Comment createComment(String uid) {
        User user = userRepository.findById(uid);
        return null;
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

    @Override
    public List<Comment> getCommentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return commentRepository.findByCommentDateTimeBetween(start, end);
    }
}
