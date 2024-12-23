package com.myfeed.service.Post;

import com.myfeed.model.post.PostReplyList;

import java.util.List;

public interface PostReplyListService {
    List<PostReplyList> findByReplyRid(long rid);

    List<PostReplyList> findByPostPid(long pid);
}
