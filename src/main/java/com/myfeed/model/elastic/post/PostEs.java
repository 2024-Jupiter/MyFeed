package com.myfeed.model.elastic.post;

import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Category;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Document(indexName = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEs {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    // 글쓴이 정보
    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Keyword)
    private String userNickName;

    @Field(type = FieldType.Keyword)
    private String userStatus;

    // 게시글 정보
    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private Category category;
    
    @Field(type = FieldType.Integer)
    private int viewCount;

    @Field(type = FieldType.Integer)
    private int likeCount;

    @Field(type = FieldType.Keyword)
    private BlockStatus blockStatus;

    @Field(type = FieldType.Date)
    private LocalDateTime blockAt;

    @Field(type = FieldType.Date)
    private LocalDateTime unBlockAt;

    @Field(type = FieldType.Keyword)
    private List<String> imageUrls;

    @Field(type = FieldType.Date)
    private LocalDateTime createAt;

    @Field(type = FieldType.Date)
    private LocalDateTime updateAt;

}