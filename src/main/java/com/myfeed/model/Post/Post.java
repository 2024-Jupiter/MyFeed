package com.myfeed.model.Post;

import com.myfeed.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @OneToMany(mappedBy = "Post", cascade = CascadeType.ALL)
    private List<PostCommentList> postCommentLists = new ArrayList<>();

    private String category;
    private String title;
    private String content;
    private String imgSrc;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int viewCount;
    private int likeCount;

    public void addPostCommentList(PostCommentList postCommentList) {
        if (this.postCommentLists == null)
            this.postCommentLists = new ArrayList<>();
        this.postCommentLists.add(postCommentList);
        postCommentList.setPost(this);
    }
}
