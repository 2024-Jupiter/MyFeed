package com.myfeed.model.reply;

import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Post;
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
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL)
    private List<PostReplyList> postReplyLists = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "pid") // 실제 외래 키 이름 확인
    private Post post;

    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    // 블락 처리
    @Enumerated(EnumType.STRING)
    private BlockStatus blockStatus = BlockStatus.NORMAL_STATUS;

    private LocalDateTime blockAt;
    private LocalDateTime unBlockAt;

    public void addPostCommentList(PostReplyList postReplyList) {
        if (this.postReplyLists == null)
            this.postReplyLists = new ArrayList<>();
        this.postReplyLists.add(postReplyList);
        postReplyList.setReply(this);
    }
}
