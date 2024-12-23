package com.myfeed.service.report;

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
        return reportRepository.findByUserUid(uid);
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

        return reportRepository.save(report);
    }

    @Override
    public Post deletePost(long pid) {
        return reportRepository.deletePost(pid);
    }

    @Override
    public Reply deleteReply(long rid) {
        return reportRepository.deleteReply(rid);
    }
}
