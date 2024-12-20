package com.myfeed.service.Post;

import com.myfeed.model.post.PostReplyList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostReplyListServiceImpl implements PostReplyListService {
    @Autowired PostReplyListService postReplyListService;

    @Override
    public List<PostReplyList> findByReplyRid(long rid) {
        return postReplyListService.findByReplyRid(rid);
    }

    @Override
    public List<PostReplyList> findByPostPid(long pid) {
        return postReplyListService.findByPostPid(pid);
    }
}
