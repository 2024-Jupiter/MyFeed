package com.myfeed.controller;

import com.myfeed.ascept.CheckPermission;
import com.myfeed.model.post.PostReplyList;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ReportType;
import com.myfeed.service.Post.PostReplyListService;
import com.myfeed.service.reply.ReplyService;
import com.myfeed.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/reply")
public class ReplyController {
    @Autowired ReplyService replyService;
    @Autowired PostReplyListService postReplyListService;
    @Autowired ReportService reportService;

    @GetMapping("/create")
    public String CreateReplyForm() {
        return "api/reply/create";
    }

    @PostMapping("/create")
    public String createReplyProc(@PathVariable long uid, @PathVariable long pid, String content) {
        replyService.createReply(uid, pid, content);
        return "redirect:/api/post/detail/" + pid;
    }

    @GetMapping("/update/{rid}")
    public String updateReplyForm(@PathVariable long rid, Model model) {
        Reply reply = replyService.findByRid(rid);
        model.addAttribute("reply", reply);
        return "api/reply/update";
    }

    @PreAuthorize("#user.id == authentication.principal.id")
    @PostMapping("/update")
    public String updateReplyProc(@RequestBody Reply reply) {
        Reply updatedReply = replyService.findByRid(reply.getRid());
        updatedReply.setContent(reply.getContent());
        updatedReply.setUpdateAt(LocalDateTime.now());
        replyService.updateReply(updatedReply);

        long pid = 0L;
        List<PostReplyList> postReplyLists = postReplyListService.getPostReplyListByReplyRid(reply.getRid());
        for (PostReplyList lists: postReplyLists) {
            pid = lists.getPost().getPid();
            break;
        }
        return "redirect:/api/post/detail/" + pid;
    }

    // @PreAuthorize("#user.id == authentication.principal.id")
    // @GetMapping("/delete/{pid}")
    // public String delete(@PathVariable long rid) {
    //     replyService.deleteReply(rid);
    //
    //     long pid = 0L;
    //     List<PostReplyList> postReplyLists = postReplyListService.getPostReplyListByPostPid(rid);
    //     for (PostReplyList lists: postReplyLists) {
    //         pid = lists.getPost().getPid();
    //         break;
    //     }
    //     return "redirect:/api/post/detail/" + pid;
    // }

    @GetMapping("/report/{rid}")
    public String saveReportForm() {
        return "api/report/save";
    }

    @PostMapping("/report")
    public String saveReportProc(ReportType reportType,
                                 @RequestParam long pid, @RequestParam long uid,
                                 @RequestParam long rid, @RequestParam(required = false) String description) {
        reportService.saveReport(reportType, pid, rid, uid, description);
        return "redirect:/api/post/detail/" + pid;
    }
}
