package com.myfeed.repository.jpa;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // 신고 상태 리스트 (차단 & 해제)
    Page<Report> findPagedReportsByStatus(Pageable pageable, ProcessStatus status);

    // 신고 게시글 리스트
    Page<Report> findPagedReportsByPost(Post post, Pageable pageable);

    // 신고 댓글 리스트
    Page<Report> findPagedReportsByReply(Reply reply, Pageable pageable);

    /*
    // 처리 대기 신고 리스트 (차단)
    Page<Report> findReportByPendingStatus(Pageable pageable, ProcessStatus status);

    // 처리 완료 신고 리스트 (차단 해제)
    Page<Report> findReportByCompletedStatus(Pageable pageable, ProcessStatus status);
     */
}
