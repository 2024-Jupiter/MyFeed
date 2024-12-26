package com.myfeed.model.post;

import com.myfeed.model.board.Tag;
import com.myfeed.model.report.ReportType;
import com.myfeed.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

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
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostReplyList> postReplyLists = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Tag tag;

    private String category;
    private String title;
    private String content;
    private String imgSrc;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int viewCount;
    private int likeCount;

    // 블락 처리
    @Enumerated(EnumType.STRING)
    private BlockStatus blockStatus = BlockStatus.NORMAL_STATUS;

    private LocalDateTime blockAt;
    private LocalDateTime unBlockAt;
}
