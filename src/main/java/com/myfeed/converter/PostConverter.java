package com.myfeed.converter;

import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.model.post.*;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.user.User;

import com.myfeed.model.user.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {
    // JPA 엔티티 -> Elasticsearch 문서 변환
    public static PostEs toElasticsearchDocument(Post post) {
        return PostEs.builder()
                .id(String.valueOf(post.getId()))
                .userId(String.valueOf(post.getUser().getId()))
                .userNickName(post.getUser().getNickname())
                .userStatus(post.getUser().isDeleted() ? "DELETED" : "ACTIVE")
                .title(post.getTitle())
                .content(post.getContent())
                .category(Category.valueOf(post.getCategory().name()))
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .blockStatus(post.getStatus())
                .blockAt(post.getStatus() == BlockStatus.BLOCK_STATUS ? post.getUpdatedAt() : null)
                .unBlockAt(post.getStatus() == BlockStatus.NORMAL_STATUS ? post.getUpdatedAt() : null)
                .imageUrls(post.getImages().stream()
                        .map(Image::getImageSrc)
                        .collect(Collectors.toList()))
                .build();
    }

    // Elasticsearch 문서 -> JPA 엔티티 변환
    public static Post toJpaEntity(PostEs postEs, User user, List<Reply> replies, List<Image> images) {
        Post post = new Post();
        post.setId(Long.parseLong(postEs.getId()));
        post.setUser(user);
        post.setTitle(postEs.getTitle());
        post.setContent(postEs.getContent());
        post.setCategory(Category.valueOf(postEs.getCategory().name()));
        post.setViewCount(postEs.getViewCount());
        post.setLikeCount(postEs.getLikeCount());
        post.setStatus(postEs.getBlockStatus());

        // Replies와 Images 설정
        if (replies != null) {
            replies.forEach(post::addReply);
        }
        if (images != null) {
            images.forEach(post::addImage);
        }

        return post;
    }
}
