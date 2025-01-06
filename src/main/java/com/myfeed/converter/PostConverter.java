package com.myfeed.converter;

import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.model.post.*;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {
    // JPA 엔티티 -> Elasticsearch 문서 변환
    public static PostEs toElasticsearchDocument(Post post) {
        return PostEs.builder()
                .id(String.valueOf(post.getId()))
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .category(Category.valueOf(post.getCategory().name()))
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
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
