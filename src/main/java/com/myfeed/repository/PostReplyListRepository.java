package com.myfeed.repository;

import com.myfeed.model.post.PostReplyList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReplyListRepository extends JpaRepository<PostReplyList, Long> {
}
