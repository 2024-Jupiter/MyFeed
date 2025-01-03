package com.myfeed.model.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDetailDto {
    private String nickname;
    private String content;
    private LocalDateTime earliestTime;

    public ReplyDetailDto(Reply reply) {
        this.nickname = reply.getUser().getNickname();
        this.content = reply.getContent();
        this.earliestTime = reply.getCreatedAt().isBefore(reply.getUpdatedAt())
                ? reply.getCreatedAt()
                : reply.getUpdatedAt();
    }
}
