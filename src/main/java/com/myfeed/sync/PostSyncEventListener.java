package com.myfeed.sync;

import com.myfeed.model.post.Image;
import com.myfeed.model.post.Post;
import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.model.user.User;
import com.myfeed.repository.jpa.PostRepository;
import com.myfeed.repository.jpa.UserRepository;
import com.myfeed.service.Post.PostEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostSyncEventListener {
    @Autowired private PostEsService postEsService;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    @Async
    @EventListener
    public void handlePostSyncEvent(PostSyncEvent event) {
        if ("CREATE_OR_UPDATE".equals(event.getOperation())) {
            Post post = postRepository.findById(event.getPostId()).orElse(null);
            User user = userRepository.findById(post.getUser().getId()).orElse(null);

            PostEs postEs = new PostEs();
            postEs.setId(String.valueOf(post.getId()));
            postEs.setUserId(String.valueOf(user.getId()));
            postEs.setUserName(user.getNickname());
            postEs.setUserStatus(user.isActive());
            postEs.setTitle(post.getTitle());
            postEs.setContent(postEs.getContent());
            postEs.setCategory(post.getCategory());
            postEs.setViewCount(post.getViewCount());
            postEs.setLikeCount(post.getLikeCount());
            postEs.setBlockStatus(post.getStatus());

            List<Image> images = post.getImages();
            List<String> imageUrls = images.stream()
                    .map(Image::getImageSrc)
                    .toList();

            postEs.setImageUrls(imageUrls);

            postEsService.syncToElasticsearch(postEs);
        } else if ("DELETE".equals(event.getOperation())) {
            postEsService.deleteFromElasticsearch(String.valueOf(event.getPostId()));
        }
    }
}