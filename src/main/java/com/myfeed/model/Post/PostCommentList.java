package com.myfeed.model.Post;

import com.myfeed.model.comment.Comment;
import com.myfeed.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "cid")
    private Comment comment;
}
