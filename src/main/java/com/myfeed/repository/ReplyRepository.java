package com.myfeed.repository;

import com.myfeed.model.reply.Reply;
import com.myfeed.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Page<Reply> findByPostContaining(Post post, Pageable pageable);
}