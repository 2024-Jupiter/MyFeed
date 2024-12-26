package com.myfeed.service.Post;

import com.myfeed.model.post.PostReplyList;

import java.util.List;

public interface PostReplyListService {
    List<PostReplyList> getPostReplyListByReplyRid(long rid);

    List<PostReplyList> getPostReplyListByPostPid(long pid);
}
