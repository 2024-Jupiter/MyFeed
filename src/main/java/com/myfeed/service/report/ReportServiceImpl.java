package com.myfeed.service.report;

import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import com.myfeed.model.report.ReportType;
import com.myfeed.repository.jpa.PostRepository;
import com.myfeed.repository.jpa.ReplyRepository;
import com.myfeed.repository.jpa.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportRepository reportRepository;
    @Autowired ReplyRepository replyRepository;
    @Autowired PostRepository postRepository;

    // 신고 불러오기
    @Override
    public Report findByRid(long rpId) {
        return reportRepository.findById(rpId).orElse(null);
    }

    // 처리 대기 신고 리스트 (차단)
    @Override
    public Page<Report> getReportByPendingStatus(int page, ProcessStatus status) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return reportRepository.findReportByStatus(pageable, status);
    }

    // 처리 완료 신고 리스트 (차단 해제)
    @Override
    public Page<Report> getReportByCompletedStatus(int page, ProcessStatus status) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return reportRepository.findReportByStatus(pageable, status);
    }

    // 게시글 신고
    @Override
    public Report reportPost(ReportType reportType, long pid, String description) {
        Post post = postRepository.findById(pid).orElse(null);

        Report report = Report.builder()
                .type(reportType).post(post).description(description).status(ProcessStatus.PENDING)
                .build();

        return reportRepository.save(report);
    }

    // 댓글 신고
    @Override
    public Report reportReply(ReportType reportType, long rid, String description) {
        Reply reply = replyRepository.findById(rid).orElse(null);

        Report report = Report.builder()
                .type(reportType).reply(reply).description(description).status(ProcessStatus.PENDING)
                .build();

        return reportRepository.save(report);
    }

    // 게시글 차단
    @Override
    public void BlockPost(long pid, long rpId) {
        Post post = postRepository.findById(pid).orElse(null);
        Report report = reportRepository.findById(rpId).orElse(null);
        post.setStatus(BlockStatus.BLOCK_STATUS);
        report.setStatus(ProcessStatus.COMPLETED);
        postRepository.save(post);
    }

    // 게시글 해제
    @Override
    public void unBlockPost(long pid, long rpId) {
        Post post = postRepository.findById(pid).orElse(null);
        Report report = reportRepository.findById(rpId).orElse(null);
        post.setStatus(BlockStatus.NORMAL_STATUS);
        report.setStatus(ProcessStatus.RELEASED);
        postRepository.save(post);
    }

    // 댓글 차단
    @Override
    public void BlockReply(long rid, long rpId) {
        Reply reply = replyRepository.findById(rid).orElse(null);
        Report report = reportRepository.findById(rpId).orElse(null);
        reply.setStatus(BlockStatus.BLOCK_STATUS);
        report.setStatus(ProcessStatus.COMPLETED);
        replyRepository.save(reply);
    }

    // 댓글 해제
    @Override
    public void unBlockReply(long rid, long rpId) {
        Reply reply = replyRepository.findById(rid).orElse(null);
        Report report = reportRepository.findById(rpId).orElse(null);
        reply.setStatus(BlockStatus.NORMAL_STATUS);
        report.setStatus(ProcessStatus.RELEASED);
        replyRepository.save(reply);
    }
}
