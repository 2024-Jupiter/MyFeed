package com.myfeed.controller;

import com.myfeed.ascept.CheckPermission;
import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import com.myfeed.service.report.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.myfeed.service.reply.ReplyService.PAGE_SIZE;

@Controller
@RequestMapping("/api/admin/report")
public class ReportController {
    @Autowired ReportService reportService;

    // 첫 화면 어떻게 할지 정해 지지 않아서 임의로 구현
    @GetMapping("/list")
    @CheckPermission("ADMIN")
    public String list(){
        return "api/admin/report/list";
    }

    // 신고 게시글 리스트 페이지네이션
    @GetMapping("/postList/{postId}")
    @CheckPermission("ADMIN")
    public String postList(@RequestParam(name="p", defaultValue = "1") int page,
                           @RequestParam Post post, HttpSession session, Model model) {
        Page<Report> reportPagedPost = reportService.getPagedReportsByPost(page, post);
        long postId = post.getId();

        int totalPages = reportPagedPost.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / reportService.PAGE_SIZE - 1) * reportService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + reportService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("currentReportPostPage", page);
        model.addAttribute("reportPagedPost", reportPagedPost.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "api/admin/report/statusList/" + postId;
    }

    // 신고 게시글 리스트 페이지네이션
    @GetMapping("/replyList/{replyId}")
    @CheckPermission("ADMIN")
    public String replyList(@RequestParam(name="p", defaultValue = "1") int page,
                             @RequestParam Reply reply, HttpSession session, Model model) {
        Page<Report> reportPagedReply = reportService.getPagedReportsByReply(page, reply);
        long replyId = reply.getId();

        int totalPages = reportPagedReply.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / reportService.PAGE_SIZE - 1) * reportService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + reportService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("currentReportPostPage", page);
        model.addAttribute("reportPagedReply", reportPagedReply.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "api/admin/report/statusList/" + replyId;
    }

    // 신고 상태 리스트(차단 - PENDING & 해제 - COMPLETED 가능) 페이지네이션
    @GetMapping("/statusList/{status}")
    @CheckPermission("ADMIN")
    public String statusList(@RequestParam(name="p", defaultValue = "1") int page,
                              @RequestParam("status") ProcessStatus status, HttpSession session, Model model) {
        Page<Report> reportPage = reportService.getPagedReportsByStatus(page, status);

        List<Report> pendingList = new ArrayList<>();
        List<Report> completedList = new ArrayList<>();
        for (Report report: reportPage.getContent()) {
            if (report.getStatus().equals(ProcessStatus.PENDING)) {
                pendingList.add(report);
            } else if (report.getStatus().equals(ProcessStatus.COMPLETED)) {
                completedList.add(report);
            }
        }

        int totalPages = reportPage.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / reportService.PAGE_SIZE - 1) * reportService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + reportService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Report> pendingPagedResult = new PageImpl<>(pendingList, pageable, reportPage.getTotalElements());
        Page<Report> completedPagedResult = new PageImpl<>(completedList, pageable, reportPage.getTotalElements());

        session.setAttribute("currentReportPostPage", page);
        model.addAttribute("pendingList", pendingPagedResult.getContent());
        model.addAttribute("completedList", completedPagedResult.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "api/admin/report/statusList/" + status;
    }

    // 상세 보기 (게시글 & 댓글 별로 맞게 보여줌)
    @GetMapping("/detail/{id}")
    public  String detail(@PathVariable long id, @RequestParam long postId, @RequestParam long replyId, Model model) {
        Report report = reportService.findByReportId(id);

        ProcessStatus status = report.getStatus();
        boolean isPostMatch = report.getPost() != null && report.getPost().getId() == postId;
        boolean isReplyMatch = report.getReply() != null && report.getReply().getId() == replyId;

        if (status == ProcessStatus.PENDING) {
            if (isPostMatch) {
                model.addAttribute("pendingPost", report);
            } else if (isReplyMatch) {
                model.addAttribute("pendingReply", report);
            }
        } else if (status == ProcessStatus.COMPLETED) {
            if (isPostMatch) {
                model.addAttribute("completedPost", report);
            } else if (isReplyMatch) {
                model.addAttribute("completedReply", report);
            }
        }

        return "api/admin/report/detail";
    }

    // 게시글 차단
    @PatchMapping("/blockPost/{postId}/{id}")
    @CheckPermission("ADMIN")
    public String BlockPost(@PathVariable long postId, @PathVariable long id) {
        reportService.BlockPost(postId, id);
        return "redirect:/api/admin/report/postList/" + postId;
    }

    // 게시글 차단 해제
    @PatchMapping("/unBlockPost/{postId}/{id}")
    @CheckPermission("ADMIN")
    public String unBlockPost(@PathVariable long postId, @PathVariable long id) {
        reportService.unBlockPost(postId, id);
        return "redirect:/api/admin/report/postList/" + postId;
    }

    // 댓글 차단
    @PatchMapping("/blockReply/{replyId}/{id}")
    @CheckPermission("ADMIN")
    public String BlockReply(@PathVariable long replyId, @PathVariable long id) {
        reportService.BlockReply(replyId, id);
        return "redirect:/api/admin/report/replyList/" + replyId;
    }

    // 댓글 차단 해제
    @PatchMapping("/unBlockReply/{replyId}/{id}")
    @CheckPermission("ADMIN")
    public String unBlockReply(@PathVariable long replyId, @PathVariable long id) {
        reportService.unBlockReply(replyId, id);
        return "redirect:/api/admin/report/replyList/" + replyId;
    }
}
