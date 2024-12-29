package com.myfeed.repository;

import com.myfeed.model.reply.Reply;
import com.myfeed.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // 게시글 내 댓글 페이지네이션
    Page<Reply> findByPostContaining(Post post, Pageable pageable);
}
