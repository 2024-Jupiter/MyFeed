package com.myfeed.converter;

import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.model.post.*;
import com.myfeed.model.reply.Reply;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostConverter {

    // JPA 엔티티 -> Elasticsearch 문서 변환
    public static PostEs toElasticsearchDocument(Post post) {
        return PostEs.builder()
                .id(String.valueOf(post.getId())) // ID 변환
                .post(Map.of(
                        "category", post.getCategory(),
                        "title", post.getTitle(),
                        "content", post.getContent(),
                        "createAt", post.getCreateAt(),
                        "updateAt", post.getUpdateAt(),
                        "viewCount", post.getViewCount(),
                        "likeCount", post.getLikeCount(),
                        "status", post.getStatus()
                ))
                .images(post.getImages().stream()
                        .map(image -> {
                            Map<String, Object> imageMap = new HashMap<>();
                            imageMap.put("id", image.getId());
                            imageMap.put("post_id", image.getPost() != null ? image.getPost().getId() : null);
                            imageMap.put("imageSrc", image.getImageSrc() != null ? image.getImageSrc().getBytes() : null);
                            return imageMap;
                        })
                        .collect(Collectors.toList()))
                .replies(post.getReplies().stream()
                        .map(reply -> {
                            Map<String, Object> replyMap = new HashMap<>();
                            replyMap.put("id", reply.getId());
                            replyMap.put("user_id", reply.getUser() != null ? reply.getUser().getId() : null);
                            replyMap.put("post_id", reply.getPost() != null ? reply.getPost().getId() : null);
                            replyMap.put("content", reply.getContent());
                            replyMap.put("createAt", reply.getCreateAt());
                            replyMap.put("updateAt", reply.getUpdateAt());
                            replyMap.put("status", reply.getStatus());
                            return replyMap;
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    // Elasticsearch 문서 -> JPA 엔티티 변환 (옵션)
    public static Post toJpaEntity(PostEs postEs, User user, List<Reply> replies, List<Image> images) {
        return Post.builder()
                .id(Long.parseLong(postEs.getId()))
                .user(user)
                .category((Category) postEs.getPost().get("category"))
                .title((String) postEs.getPost().get("title"))
                .content((String) postEs.getPost().get("content"))
                .images(images)
                .replies(replies)
                .createAt((LocalDateTime) postEs.getPost().get("createAt"))
                .updateAt((LocalDateTime) postEs.getPost().get("updateAt"))
                .viewCount((int) postEs.getPost().get("viewCount"))
                .likeCount((int) postEs.getPost().get("likeCount"))
                .build();
    }
}