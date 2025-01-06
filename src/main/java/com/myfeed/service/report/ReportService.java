package com.myfeed.service.report;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import com.myfeed.model.report.ReportDto;
import com.myfeed.model.report.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    public static final int PAGE_SIZE = 10;

    // 신고 불러 오기
    Report findByReportId(Long id);

    // 신고 게시글 페이지 네이션 (동시성)
    Page<Report> getPagedReportsByPost(int page, Post post);

    // 게시글 신고 상세 보기
    List<Report> getReportsByPost(Long postId);

    // 신고 댓글 페이지 네이션 (동시성)
    Page<Report> getPagedReportsByReply(int page, Reply reply);

    // 댓글 신고 상세 보기
    List<Report> getReportsByReply(Long replyId);

    // 게시글 신고
    Report reportPost(Long postId, ReportDto reportDto);

    // 댓글 신고
    Report reportReply(Long replyId, ReportDto reportDto);

    // 게시글 차단
    void BlockPost(Long id);

    // 게시글 해제
    void unBlockPost(Long id);

    // 댓글 차단
    void BlockReply(Long id);

    // 댓긍 해제
    void unBlockReply(Long id);
}
