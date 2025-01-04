package com.myfeed.service.reply;

import com.myfeed.exception.PostBlockedException;
import com.myfeed.exception.ReplyBlockedException;
import com.myfeed.exception.UserDeletedException;
import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.post.Post;
import com.myfeed.model.reply.ReplyDto;
import com.myfeed.model.user.User;
import com.myfeed.repository.jpa.ReplyRepository;
import com.myfeed.repository.jpa.PostRepository;
import com.myfeed.repository.jpa.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired ReplyRepository replyRepository;

    // 댓글 가져 오기
    @Override
    public Reply findByReplyId(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    // 댓글 작성
    @Override
    public Reply createReply(Long userId, Long postId, ReplyDto replyDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (post.getStatus() == BlockStatus.BLOCK_STATUS) {
            throw new PostBlockedException("차단된 게시글 입니다.");
        }

        Reply reply = Reply.builder()
                .user(user).post(post).content(replyDto.getContent())
                .status(BlockStatus.NORMAL_STATUS)
                .build();
        post.addReply(reply);

        return replyRepository.save(reply);
    }

    // 게시글 내의 댓글 리스트 (동시성)
    @Override
    public Page<Reply> getPagedRepliesByPost(int page, Post post) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("createdDate").descending());
        Page<Reply> replies = replyRepository.findPagedRepliesByPost(post, pageable);

        for (Reply reply : replies) {
            if (reply.getStatus() == BlockStatus.BLOCK_STATUS) {
                throw new ReplyBlockedException("차단된 댓글이 포함 되어 있습니다.");
            }
            if (reply.getUser() == null || reply.getUser().isDeleted()) {
                throw new UserDeletedException("삭제된 사용자의 댓글이 포함 되어 있습니다.");
            }
        }

        List<Reply> filteredReplies = replies.getContent().stream()
                .filter(reply -> reply.getUser() != null && !reply.getUser().isDeleted())
                .toList();

        return new PageImpl<>(filteredReplies, pageable, replies.getTotalElements());
    }

    // 댓글 수정
    @Override
    public void updateReply (Long id, ReplyDto replyDto) {
        Reply reply = findByReplyId(id);

        if (reply.getPost().getStatus() == BlockStatus.BLOCK_STATUS) {
            throw new PostBlockedException("차단된 게시글 입니다.");
        }
        if (reply.getStatus() == BlockStatus.BLOCK_STATUS) {
            throw new PostBlockedException("차단된 댓글 입니다.");
        }

        reply.setContent(replyDto.getContent());
        replyRepository.save(reply);
    }

    // 댓글 삭제
    @Override
    public void deleteReply (Long id) {
         replyRepository.deleteById(id);
    }
}
