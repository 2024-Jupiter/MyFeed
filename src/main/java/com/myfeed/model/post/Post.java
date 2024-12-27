package com.myfeed.model.post;
import com.myfeed.model.reply.Reply;
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
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    private Category category;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int viewCount;
    private int likeCount;

    // 블락 처리
    @Enumerated(EnumType.STRING)
    private BlockStatus status = BlockStatus.NORMAL_STATUS;

    public void addReply(Reply reply) {
        if (this.replies == null)
            this.replies = new ArrayList<>();
        this.replies.add(reply);
        reply.setPost(this);
    }

    public void addImage(Image image) {
        if (this.images == null)
            this.images = new ArrayList<>();
        this.images.add(image);
        image.setPost(this);
    }
}
