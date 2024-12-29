package com.myfeed.controller;

import com.myfeed.model.post.Post;
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
    @Autowired ReplyService replyService;
    @Autowired ReportService reportService;

    // 댓글 작성
    @GetMapping("/create")
    public String CreateReplyForm() {
        return "api/reply/create";
    }
    // 댓글 작성
    @PostMapping("/create")
    public String createReplyProc(@PathVariable long uid, @PathVariable long pid, @RequestParam String content) {
        replyService.createReply(uid, pid, content);
        return "redirect:/api/post/detail/" + pid;
    }

    // 댓글 수정
    @GetMapping("/update/{rid}")
    public String updateReplyForm(@PathVariable long rid, Model model) {
        Reply reply = replyService.findByRid(rid);
        model.addAttribute("reply", reply);
        return "api/reply/update";
    }
    // 댓글 수정
    @PreAuthorize("#user.id == authentication.principal.id")
    @PostMapping("/update")
    public String updateReplyProc(@RequestBody Reply reply) {
        Reply updatedReply = replyService.findByRid(reply.getId());
        updatedReply.setContent(reply.getContent());
        replyService.updateReply(updatedReply);

        long pid = updatedReply.getPost().getId();
        return "redirect:/api/post/detail/" + pid;
    }

    // 댓글 삭제
    @PreAuthorize("#user.id == authentication.principal.id")
    @GetMapping("/delete/{pid}")
    public String delete(@PathVariable long rid) {
        replyService.deleteReply(rid);
        Reply reply = replyService.findByRid(rid);
        long pid = reply.getPost().getId();
        return "redirect:/api/post/detail/" + pid;
    }

    // 신고
    @GetMapping("/report/{rid}")
    public String saveReportForm() {
        return "api/report/save";
    }
    // 신고 (삭제된 사용자는 신고 접수 불가)
    @PostMapping("/report")
    public String saveReportProc(ReportType reportType, @RequestParam long rid,
                                 @RequestParam(required = false) String description, Model model) {
        Reply reply = replyService.findByRid(rid);
        if (!reply.getUser().isDeleted()) {
            reportService.reportPost(reportType, rid, description);
            model.addAttribute("message", "댓글 신고가 성공적으로 처리되었습니다.");
        } else {
            model.addAttribute("message", "삭제된 사용자입니다.");
        }
        return "redirect:/api/post/detail/" + reply.getPost().getId();
    }
}
