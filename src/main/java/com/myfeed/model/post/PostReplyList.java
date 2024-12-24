package com.myfeed.model.post;

import com.myfeed.model.reply.Reply;
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
public class PostReplyList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long lid;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "rid", nullable = true)
    private Reply reply;
}
