package com.myfeed.controller;

import com.myfeed.ascept.CheckPermission;
import com.myfeed.model.report.ProcessStatus;
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

    // 첫 화면 어떻게 할지 정해 지지 않아서 임의로 구현
    @GetMapping("list")
    @CheckPermission("ADMIN")
    public String list(){
        return "api/admin/report/list";
    }

    // 신고 대기 리스트(차단 가능) 페이지네이션 - PENDING
    @GetMapping("/pendingList/{status}")
    @CheckPermission("ADMIN")
    public String pendingList(@RequestParam(name="p", defaultValue = "1") int page,
                              @RequestParam("status") ProcessStatus status, HttpSession session, Model model) {
        Page<Report> reportPostPage = reportService.getReportByPendingStatus(page, status);

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
        return "api/admin/report/pendingList/" + status;
    }

    // 신고 완료 리스트(해제 가능) 페이지네이션 - COMPLETED
    @GetMapping("/completedList/{status}")
    @CheckPermission("ADMIN")
    public String reportPost(@RequestParam(name="p", defaultValue = "1") int page,
                             @RequestParam("status") ProcessStatus status, HttpSession session, Model model) {
        Page<Report> reportPostPage = reportService.getReportByCompletedStatus(page, status);

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
        return "api/admin/report/completedList/" + status;
    }

    // 게시글 차단
    @GetMapping("BlockPost/{pid}")
    @CheckPermission("ADMIN")
    public String BlockPost(@PathVariable long pid, @PathVariable long rpId) {
        reportService.BlockPost(pid, rpId);
        return "redirect:/api/admin/report/postList/" + pid;
    }

    // 게시글 차단 해제
    @GetMapping("unBlockPost/{pid}")
    @CheckPermission("ADMIN")
    public String unBlockPost(@PathVariable long pid, @PathVariable long rpId) {
        reportService.unBlockPost(pid, rpId);
        return "redirect:/api/admin/report/postList/" + pid;
    }

    // 댓글 차단
    @GetMapping("BlockReply/{rid}")
    @CheckPermission("ADMIN")
    public String BlockReply(@PathVariable long rid, @PathVariable long rpId) {
        reportService.BlockReply(rid, rpId);
        return "redirect:/api/admin/report/replyList/" + rid;
    }

    // 댓글 차단 해제
    @GetMapping("unBlockReply/{rid}")
    @CheckPermission("ADMIN")
    public String unBlockReply(@PathVariable long rid, @PathVariable long rpId) {
        reportService.unBlockReply(rid, rpId);
        return "redirect:/api/admin/report/replyList/" + rid;
    }
}
