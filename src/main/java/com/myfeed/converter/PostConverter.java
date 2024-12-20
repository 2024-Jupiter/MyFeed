package com.myfeed.converter;

import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostCommentList;
import com.myfeed.model.post.PostEs;
import com.myfeed.model.user.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostConverter {

    // JPA 엔티티 -> Elasticsearch 문서 변환
    public static PostEs toElasticsearchDocument(Post post) {
        return PostEs.builder()
                .postId(String.valueOf(post.getPid())) // ID 변환
                .post(Map.of(
                        "category", post.getCategory(),
                        "title", post.getTitle(),
                        "content", post.getContent(),
                        "imgSrc", post.getImgSrc(),
                        "createAt", post.getCreateAt(),
                        "updateAt", post.getUpdateAt(),
                        "viewCount", post.getViewCount(),
                        "likeCount", post.getLikeCount()
                ))
                .comments(post.getPostCommentLists().stream()
                        .map(comment -> {
                            Map<String, Object> commentMap = new HashMap<>();
                            commentMap.put("lid", comment.getLid());
                            commentMap.put("uid", comment.getUser() != null ? comment.getUser().getId() : null);
                            commentMap.put("Reply", comment.getReply() != null ? comment.getReply().getContent() : null);
                            return commentMap;
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    // Elasticsearch 문서 -> JPA 엔티티 변환 (옵션)
    public static Post toJpaEntity(PostEs postEs, User user, List<PostCommentList> comments) {
        return Post.builder()
                .pid(Long.parseLong(postEs.getPostId()))
                .user(user)
                .category((String) postEs.getPost().get("category"))
                .title((String) postEs.getPost().get("title"))
                .content((String) postEs.getPost().get("content"))
                .imgSrc((String) postEs.getPost().get("imgSrc"))
                .createAt((LocalDateTime) postEs.getPost().get("createAt"))
                .updateAt((LocalDateTime) postEs.getPost().get("updateAt"))
                .viewCount((int) postEs.getPost().get("viewCount"))
                .likeCount((int) postEs.getPost().get("likeCount"))
                .postCommentLists(comments)
                .build();
    }
}
