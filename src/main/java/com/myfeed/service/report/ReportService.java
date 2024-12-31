package com.myfeed.service.report;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import com.myfeed.model.report.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    public static final int PAGE_SIZE = 10;

    // 신고 불러 오기
    Report findByReportId(long id);

    // 처리 대기 신고 리스트 (차단 & 해제)
    Page<Report> getPagedReportsByStatus(int page, ProcessStatus status);

    // 신고 게시글 리스트
    Page<Report> getPagedReportsByPost(int page, Post post);

    // 신고 댓글 리스트
    Page<Report> getPagedReportsByReply(int page, Reply reply);

    // 게시글 신고
    Report reportPost(ReportType type, long postId, String description);

    // 댓글 신고
    Report reportReply(ReportType type, long replyId, String description);

    // 게시글 차단
    void BlockPost(long postId, long id);

    // 게시글 해제
    void unBlockPost(long postId, long id);

    // 댓글 차단
    void BlockReply(long replyId, long id);

    // 댓긍 해제
    void unBlockReply(long replyId, long id);
}
