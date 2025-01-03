package com.myfeed.model.reply;

import com.myfeed.model.post.ImageDto;
import com.myfeed.model.post.Post;
import com.myfeed.model.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyReportDto {
    private UserDto userDto;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReplyReportDto(Reply reply) {
        this.userDto = new UserDto(reply.getUser());
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();
    }
}
