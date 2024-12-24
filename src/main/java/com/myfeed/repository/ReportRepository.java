package com.myfeed.repository;

import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostReplyList;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPostPid(long pid);

    List<Report> findByUserId(long uid);

    List<Report> findByReplyRid(long rid);

    // Post deletePost(long pid);

    // Reply deleteReply(long rid);
}
