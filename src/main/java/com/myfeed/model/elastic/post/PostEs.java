package com.myfeed.model.elastic.post;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Setter
public class PostEs {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Field(type = FieldType.Keyword)
    private String nickname;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Field(type = FieldType.Date)
    private String createAt;

}
