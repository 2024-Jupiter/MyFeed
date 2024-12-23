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
@RequestMapping("/api/admin/report")
public class ReportController {
    @Autowired ReportService reportService;

    @GetMapping("list")
    @CheckPermission("ADMIN")
    public String list(){
        return "api/admin/report/list";
    }

    @GetMapping("/postList/{pid}")
    @CheckPermission("ADMIN")
    public String reportPost(@PathVariable long pid, Model model) {
        List<Report> reportPostList = reportService.getReportByPostPid(pid);
        model.addAttribute(reportPostList);
        return "api/admin/report/postList/" + pid;
    }

    @GetMapping("/replyList/{rid}")
    @CheckPermission("ADMIN")
    public String reportReply(@PathVariable long rid, Model model) {
        List<Report> reportReplyList = reportService.getReportByReplyRid(rid);
        model.addAttribute(reportReplyList);
        return "api/admin/report/replyList/" + rid;
    }

    @GetMapping("unBlockPost/{pid}")
    @CheckPermission("ADMIN")
    public String unBlockPost(@PathVariable long pid) {
        reportService.unBlockPost(pid);
        return "redirect:/api/admin/report/postList/" + pid;
    }

    @GetMapping("unBlockReply/{rid}")
    @CheckPermission("ADMIN")
    public String unBlockReply(@PathVariable long rid) {
        reportService.unBlockReply(rid);
        return "redirect:/api/admin/report/replyList/" + rid;
    }
}
