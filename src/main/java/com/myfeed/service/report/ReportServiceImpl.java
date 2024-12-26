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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired ReportRepository reportRepository;
    @Autowired UserRepository userRepository;
    @Autowired ReplyRepository replyRepository;
    @Autowired PostRepository postRepository;

    @Override
    public Report findByRid(long rpId) {
        return reportRepository.findById(rpId).orElse(null);
    }

    @Override
    public List<Report> getReportByPostPid(long pid) {
        return reportRepository.findByPostPid(pid);
    }

    @Override
    public List<Report> getReportByReplyRid(long rid) {
        return reportRepository.findByReplyRid(rid);
    }

    @Override
    public List<Report> getReportByUserUid(long uid) {
        return reportRepository.findByUserId(uid);
    }

    @Override
    public Report saveReport(ReportType reportType, long pid, long uid, long rid, String description) {
        Post post = postRepository.findById(pid).orElse(null);
        User user = userRepository.findById(uid).orElse(null);
        Reply reply = replyRepository.findById(rid).orElse(null);

        Report report = Report.builder()
                .reportType(reportType).post(post).user(user).reply(reply)
                .description(description).reportedAt(LocalDateTime.now())
                .build();

        Report savedReport = reportRepository.save(report);

        if (reply == null) {
            post.setBlockStatus(BlockStatus.BLOCK_STATUS);
            post.setBlockAt(LocalDateTime.now());
            postRepository.save(post);
        } else {
            reply.setBlockStatus(BlockStatus.BLOCK_STATUS);
            reply.setBlockAt(LocalDateTime.now());
            replyRepository.save(reply);
        }

        return savedReport;
    }

    @Override
    public void unBlockPost(long pid) {
        Post post = postRepository.findById(pid).orElse(null);
        post.setBlockStatus(BlockStatus.NORMAL_STATUS);
        post.setUnBlockAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Override
    public void unBlockReply(long rid) {
        Reply reply = replyRepository.findById(rid).orElse(null);
        reply.setBlockStatus(BlockStatus.NORMAL_STATUS);
        reply.setUnBlockAt(LocalDateTime.now());
        replyRepository.save(reply);
    }

    @Override
    public void unBlockUser(long uid) {
        User user = userRepository.findById(uid).orElse(null);
        user.setActive(true);
        userRepository.save(user);
    }

    // 사용자 비활성화
    @Override
    public void blockUser(long uid) {
        User user = userRepository.findById(uid).orElse(null);
        user.setActive(false);
        userRepository.save(user);
    }
}
