package com.myfeed.model.report;

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
public class ReportDto {
    @NotBlank(message = "신고 유형을 선택하세요.")
    private String type;

    @Size(max = 100, message = "신고 내용은 최대 100자까지 가능합니다.")
    private String description;
}
