package com.myfeed.controller;

import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ReportType;
import com.myfeed.service.reply.ReplyService;
import com.myfeed.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/reply")
public class ReplyController {
    @Autowired
    private ReplyService replyService;
    @Autowired
    private ReportService reportService;

    // 댓글 작성 폼 (GET 요청으로 폼을 가져옴)
    @GetMapping("/create")
    public String createReplyForm() {
        return "api/reply/create";
    }

    // 댓글 작성 (POST 요청으로 댓글 생성)
    @PostMapping("/create")
    public String createReply(@RequestParam long userId, @RequestParam long postId, @RequestParam String content) {
        replyService.createReply(userId, postId, content);
        return "redirect:/api/post/detail/" + postId;
    }

    // 댓글 수정 폼 (GET 요청으로 댓글 수정 폼을 가져옴)
    @GetMapping("/update/{id}")
    public String updateReplyForm(@PathVariable long id, Model model) {
        Reply reply = replyService.findByReplyId(id);
        model.addAttribute("reply", reply);
        return "api/reply/update";
    }

    // 댓글 수정 (PATCH 요청으로 댓글 수정)
    @PatchMapping("/update/{id}")
    @PreAuthorize("#user.id == authentication.principal.id")
    public String updateReply(@PathVariable long id, @RequestParam String content) {
        Reply reply = replyService.findByReplyId(id);
        reply.setContent(content);
        replyService.updateReply(reply);

        long postId = reply.getPost().getId();
        return "redirect:/api/post/detail/" + postId;
    }

    // 댓글 삭제 (DELETE 요청으로 댓글 삭제)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("#user.id == authentication.principal.id")
    public String deleteReply(@PathVariable long id) {
        replyService.deleteReply(id);
        Reply reply = replyService.findByReplyId(id);
        long postId = reply.getPost().getId();
        return "redirect:/api/post/detail/" + postId;
    }

    // 댓글 신고 폼 (GET 요청으로 신고 폼을 가져옴)
    @GetMapping("/report")
    public String reportForm() {
        return "api/report/save";
    }

    // 댓글 신고 (POST 요청으로 신고 처리)
    @PostMapping("/report")
    public String reportReply(@RequestParam ReportType type, @RequestParam long replyId,
                              @RequestParam(required = false) String description, Model model) {
        Reply reply = replyService.findByReplyId(replyId);
        if (!reply.getUser().isDeleted()) {
            reportService.reportReply(type, replyId, description);
            model.addAttribute("message", "댓글 신고가 성공적으로 처리되었습니다.");
        } else {
            model.addAttribute("message", "삭제된 사용자입니다.");
        }
        return "redirect:/api/post/detail/" + reply.getPost().getId();
    }
}

