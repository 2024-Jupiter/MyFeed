package com.myfeed.service.report;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import com.myfeed.model.report.ReportType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {
    public static final int PAGE_SIZE = 10;

    // 신고 불러 오기
    Report findByRid(long rpId);

    // 처리 대기 신고 리스트 (차단)
    Page<Report> getReportByPendingStatus(int page, ProcessStatus status);

    // 처리 완료 신고 리스트 (차단 해제)
    Page<Report> getReportByCompletedStatus(int page, ProcessStatus status);

    // 게시글 신고
    Report reportPost(ReportType reportType, long pid, String description);

    // 댓글 신고
    Report reportReply(ReportType reportType, long rid, String description);

    // 게시글 차단
    void BlockPost(long pid, long rpId);

    // 게시글 해제
    void unBlockPost(long pid, long rpId);

    // 댓글 차단
    void BlockReply(long rid, long rpId);

    // 댓긍 해제
    void unBlockReply(long rid, long rpId);
}
