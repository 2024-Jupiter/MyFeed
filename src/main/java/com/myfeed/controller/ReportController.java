package com.myfeed.controller;

import com.myfeed.ascept.CheckPermission;
import com.myfeed.model.report.Report;
import com.myfeed.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/report")
public class ReportController {
    @Autowired ReportService reportService;

    @GetMapping("list")
    @CheckPermission("ADMIN")
    public String list(){
        return "report/list";
    }

    @GetMapping("/userList/{uid}")
    @CheckPermission("ADMIN")
    public String reportUser(@PathVariable long uid, Model model) {
        List<Report> reportUserList = reportService.getReportByUserUid(uid);
        model.addAttribute(reportUserList);
        return "api/report/userList/" + uid;
    }

    @GetMapping("/postList/{pid}")
    @CheckPermission("ADMIN")
    public String reportPost(@PathVariable long pid, Model model) {
        List<Report> reportPostList = reportService.getReportByReplyRid(pid);
        model.addAttribute(reportPostList);
        return "api/report/postList/" + pid;
    }

    @GetMapping("/replyList/{rid}")
    @CheckPermission("ADMIN")
    public String reportReply(@PathVariable long rid, Model model) {
        List<Report> reportReplyList = reportService.getReportByReplyRid(rid);
        model.addAttribute(reportReplyList);
        return "api/report/replyList/" + rid;
    }

    // block 처리 & 신고 처리 시간
    @GetMapping("/deletePost/{pid}")
    public String deletePost(@PathVariable long pid) {
        reportService.deletePost(pid);
        return "redirect:/api/report/postList/" + pid;
    }

    @GetMapping("/deleteReply/{rid}")
    public String deleteReply(@PathVariable long rid) {
        reportService.deleteReply(rid);
        return "redirect:/api/report/replyList/" + rid;
    }
}
