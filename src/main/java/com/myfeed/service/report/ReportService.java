package com.myfeed.service.report;

import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.Report;
import com.myfeed.model.report.ReportType;

import java.util.List;

public interface ReportService {
    Report findByRid(long rpId);

    List<Report> getReportByPostPid(long pid);

    List<Report> getReportByReplyRid(long rid);

    List<Report> getReportByUserUid(long uid);

    Report saveReport(ReportType reportType, long pid, long uid, long rid, String description);

    void unBlockPost(long pid);

    void unBlockReply(long rid);

    void unBlockUser(long uid);

    void blockUser(long uid);
}
