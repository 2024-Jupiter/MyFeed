package com.myfeed.service.report;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.Report;
import com.myfeed.model.report.ReportType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {
    public static final int PAGE_SIZE = 10;

    Report findByRid(long rpId);

    Page<Report> getReportByPostPid(int page, long pid);

    Page<Report> getReportByReplyRid(int page, long rid);

    Report reportPost(ReportType reportType, long pid, String description);

    Report reportReply(ReportType reportType, long rid, String description);

    void BlockPost(long pid, long rpId);

    void unBlockPost(long pid, long rpId);

    void BlockReply(long rid, long rpId);

    void unBlockReply(long rid, long rpId);
}
