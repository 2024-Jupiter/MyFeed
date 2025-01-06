package com.myfeed.model.elastic;

import com.myfeed.model.post.Category;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostEsDto1 {

    private String id;
    private Category category;
    private String userName;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private String _class;
    private String nickname;
    private String createAt;


}
