package com.myfeed.model.post;

import com.myfeed.model.reply.ReplyDetailDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(indexName = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEs {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    // 글쓴이 정보
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

    @Field(type = FieldType.Date)
    private LocalDateTime createAt;

    // 댓글
    @Field(type = FieldType.Nested)
    private List<Map<String, Object>> replies;
}
