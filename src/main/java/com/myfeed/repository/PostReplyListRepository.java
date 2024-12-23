package com.myfeed.repository;

import com.myfeed.model.post.PostReplyList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostReplyListRepository extends JpaRepository<PostReplyList, Long> {
    List<PostReplyList> findByPostPid(long pid);

    List<PostReplyList> findByReplyRid(long rid);
}
