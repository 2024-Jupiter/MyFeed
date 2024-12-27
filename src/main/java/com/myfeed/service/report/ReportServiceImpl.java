package com.myfeed.service.report;

import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.Report;
import com.myfeed.model.report.ReportType;
import com.myfeed.model.user.User;
import com.myfeed.repository.PostRepository;
import com.myfeed.repository.ReplyRepository;
import com.myfeed.repository.ReportRepository;
import com.myfeed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired ReportRepository reportRepository;
    @Autowired ReplyRepository replyRepository;
    @Autowired PostRepository postRepository;

    @Override
    public Report findByRid(long rpId) {
        return reportRepository.findById(rpId).orElse(null);
    }

    @Override
    public Page<Report> getReportByPostPid(int page, long pid) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return reportRepository.findByPostPid(pid, pageable);
    }

    @Override
    public Page<Report> getReportByReplyRid(int page, long rid) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return reportRepository.findByReplyRid(rid, pageable);
    }

    @Override
    public Report reportPost(ReportType reportType, long pid, String description) {
        Post post = postRepository.findById(pid).orElse(null);

        Report report = Report.builder()
                .reportType(reportType).post(post)
                .description(description).reportedAt(LocalDateTime.now())
                .build();

        return reportRepository.save(report);
    }

    @Override
    public Report reportReply(ReportType reportType, long rid, String description) {
        Reply reply = replyRepository.findById(rid).orElse(null);

        Report report = Report.builder()
                .reportType(reportType).reply(reply)
                .description(description).reportedAt(LocalDateTime.now())
                .build();

        return reportRepository.save(report);
    }

    @Override
    public void BlockPost(long pid) {
        Post post = postRepository.findById(pid).orElse(null);
        post.setBlockStatus(BlockStatus.BLOCK_STATUS);
        postRepository.save(post);
    }

    @Override
    public void unBlockPost(long pid) {
        Post post = postRepository.findById(pid).orElse(null);
        post.setBlockStatus(BlockStatus.NORMAL_STATUS);
        postRepository.save(post);
    }

    @Override
    public void BlockReply(long rid) {
        Reply reply = replyRepository.findById(rid).orElse(null);
        reply.setBlockStatus(BlockStatus.BLOCK_STATUS);
        replyRepository.save(reply);
    }

    @Override
    public void unBlockReply(long rid) {
        Reply reply = replyRepository.findById(rid).orElse(null);
        reply.setBlockStatus(BlockStatus.NORMAL_STATUS);
        replyRepository.save(reply);
    }
}
