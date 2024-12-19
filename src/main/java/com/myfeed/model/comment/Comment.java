package com.myfeed.model.comment;

import com.myfeed.model.post.PostCommentList;
import com.myfeed.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @OneToMany(mappedBy = "Comment", cascade = CascadeType.ALL)
    private List<PostCommentList> postCommentLists = new ArrayList<>();

    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void addPostCommentList(PostCommentList postCommentList) {
        if (this.postCommentLists == null)
            this.postCommentLists = new ArrayList<>();
        this.postCommentLists.add(postCommentList);
        postCommentList.setComment(this);
    }
}
