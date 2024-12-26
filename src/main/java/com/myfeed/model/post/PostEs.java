package com.myfeed.model.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Map;

@Document(indexName = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEs {
    @Id
    private String postId;

    // 게시글 정보
    @Field(type = FieldType.Object)
    private Map<String, Object> post;

    // 댓글
    @Field(type = FieldType.Nested)
    private List<Map<String, Object>> comments;
}
