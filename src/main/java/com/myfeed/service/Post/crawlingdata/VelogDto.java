package com.myfeed.service.Post.crawlingdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VelogDto {

    private Long id;

    private String title;

    private String userName;

    private String content;

    private String date;
    private int likeCount;
    private List<VelogCommentDto> comments;
}
