package com.myfeed.model.reply;

import com.myfeed.model.post.PostReplyList;
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
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @OneToMany(mappedBy = "Reply", cascade = CascadeType.ALL)
    private List<PostReplyList> postReplyLists = new ArrayList<>();

    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void addPostCommentList(PostReplyList postReplyList) {
        if (this.postReplyLists == null)
            this.postReplyLists = new ArrayList<>();
        this.postReplyLists.add(postReplyList);
        postReplyList.setReply(this);
    }
}
