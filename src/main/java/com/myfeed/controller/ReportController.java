package com.myfeed.controller;

import com.myfeed.ascept.CheckPermission;
import com.myfeed.model.report.Report;
import com.myfeed.service.report.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

    // 신고 게시글 페이지네이션
    @GetMapping("/postList/{pid}")
    @CheckPermission("ADMIN")
    public String reportPost(@RequestParam(name="p", defaultValue = "1") int page,
                             @PathVariable long pid, HttpSession session, Model model) {
        Page<Report> reportPostPage = reportService.getReportByPostPid(page, pid);

        int totalPages = reportPostPage.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / reportService.PAGE_SIZE - 1) * reportService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + reportService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("currentReportPostPage", page);
        model.addAttribute("reportPostList", reportPostPage.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "api/admin/report/postList/" + pid;
    }

    // 신고 댓글 페이지네이션
    @GetMapping("/replyList/{rid}")
    @CheckPermission("ADMIN")
    public String reportReply(@RequestParam(name="p", defaultValue = "1") int page,
                              @PathVariable long rid, HttpSession session, Model model) {
        Page<Report> reportReplyPage = reportService.getReportByReplyRid(page, rid);

        int totalPages = reportReplyPage.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / reportService.PAGE_SIZE - 1) * reportService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + reportService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("currentReportReplyPage", page);
        model.addAttribute("reportReplyList", reportReplyPage.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "api/admin/report/replyList/" + rid;
    }

    @GetMapping("BlockPost/{pid}")
    @CheckPermission("ADMIN")
    public String BlockPost(@PathVariable long pid, @PathVariable long rpId) {
        reportService.BlockPost(pid, rpId);
        return "redirect:/api/admin/report/postList/" + pid;
    }
    @GetMapping("unBlockPost/{pid}")
    @CheckPermission("ADMIN")
    public String unBlockPost(@PathVariable long pid, @PathVariable long rpId) {
        reportService.unBlockPost(pid, rpId);
        return "redirect:/api/admin/report/postList/" + pid;
    }

    @GetMapping("BlockReply/{rid}")
    @CheckPermission("ADMIN")
    public String BlockReply(@PathVariable long rid, @PathVariable long rpId) {
        reportService.BlockReply(rid, rpId);
        return "redirect:/api/admin/report/replyList/" + rid;
    }

    @GetMapping("unBlockReply/{rid}")
    @CheckPermission("ADMIN")
    public String unBlockReply(@PathVariable long rid, @PathVariable long rpId) {
        reportService.unBlockReply(rid, rpId);
        return "redirect:/api/admin/report/replyList/" + rid;
    }
}
