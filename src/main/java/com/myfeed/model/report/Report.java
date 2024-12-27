package com.myfeed.model.report;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Enumerated(EnumType.STRING)
    private ProcessStatus status = ProcessStatus.RELEASED;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = true)
    private Reply reply;

    @Column(length = 500)
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
