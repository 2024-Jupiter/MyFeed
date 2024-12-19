package com.myfeed.model.post;

import com.myfeed.model.comment.Comment;
import com.myfeed.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long lid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "cid", nullable = true)
    private Comment comment;
}
