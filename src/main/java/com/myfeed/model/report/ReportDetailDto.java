package com.myfeed.model.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDetailDto {
    private Long id;
    private String type;
    private String status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReportDetailDto(Report report) {
        this.id = report.getId();
        this.type = String.valueOf(report.getType());
        this.status = String.valueOf(report.getStatus());
        this.description = report.getDescription();
        this.createdAt = report.getCreatedAt();
        this.updatedAt = report.getUpdatedAt();
    }
}
