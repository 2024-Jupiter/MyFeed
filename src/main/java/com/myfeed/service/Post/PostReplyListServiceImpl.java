package com.myfeed.service.Post;

import com.myfeed.model.post.PostReplyList;
import com.myfeed.repository.PostReplyListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostReplyListServiceImpl implements PostReplyListService {
    @Autowired PostReplyListRepository postReplyListRepository;

    @Override
    public List<PostReplyList> getPostReplyListByReplyRid(long rid) {
        return postReplyListRepository.findByReplyRid(rid);
    }

    @Override
    public List<PostReplyList> getPostReplyListByPostPid(long pid) {
        return postReplyListRepository.findByPostPid(pid);
    }
}
