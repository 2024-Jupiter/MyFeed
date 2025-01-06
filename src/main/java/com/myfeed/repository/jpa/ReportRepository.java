package com.myfeed.repository.jpa;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // 신고 게시글 페이지 네이션 (동시성)
    @Query("SELECT r FROM Report r WHERE r.post.user.isDeleted = false")
    Page<Report> findPagedReportsByPost(Post post, Pageable pageable);

    // 게시글 신고 내역 조회
    List<Report> findReportByPostId(Long postId);

    // 신고 댓글 페이지 네이션 (동시성)
    @Query("SELECT r FROM Report r WHERE r.reply.user.isDeleted = false")
    Page<Report> findPagedReportsByReply(Reply reply, Pageable pageable);

    // 댓글 신고 내역 조회
    List<Report> findReportByReplyId(Long replyId);
}
