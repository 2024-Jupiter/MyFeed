package com.myfeed.model.reply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDto {
    @NotBlank(message = "내용을 입력하세요.")
    @Size(min = 1, max = 500, message = "한 글자 이상 내용을 입력하세요.")
    private String content;
}
