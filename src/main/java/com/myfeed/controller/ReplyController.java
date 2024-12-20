package com.myfeed.controller;

import com.myfeed.model.post.PostReplyList;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostReplyListService;
import com.myfeed.service.reply.ReplyService;
import com.myfeed.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/create")
    public String CreateReplyForm() {
        return "api/reply/create";
    }

    @PostMapping("/create")
    public String createReplyProc(@PathVariable long uid, @PathVariable long pid, String content) {
        replyService.createReply(uid, pid, content);
        return "redirect:/api/post/detail/" + pid;
    }

    // listAll(관리자) - 신고된 글 list(enum)


    @GetMapping("/update/{rid}")
    public String updateReplyForm(@PathVariable long rid, Model model) {
        Reply reply = replyService.findByRid(rid);
        model.addAttribute("reply", reply);
        return "api/reply/update";
    }

    @PostMapping("/update")
    public String updateReplyProc(@RequestBody Reply reply) {
        Reply updatedReply = replyService.findByRid(reply.getRid());
        updatedReply.setContent(reply.getContent());
        updatedReply.setUpdateAt(LocalDateTime.now());
        replyService.updateReply(updatedReply);

        long pid = 0L;
        List<PostReplyList> postReplyLists = postReplyListService.findByReplyRid(reply.getRid());
        for (PostReplyList lists: postReplyLists) {
            pid = lists.getPost().getPid();
            break;
        }
        return "redirect:/api/post/detail" + pid;
    }

    @GetMapping("/delete/{pid}")
    public String delete(@PathVariable long rid) {
        replyService.deleteReply(rid);

        long pid = 0L;
        List<PostReplyList> postReplyLists = postReplyListService.findByReplyRid(rid);
        for (PostReplyList lists: postReplyLists) {
            pid = lists.getPost().getPid();
            break;
        }
        return "redirect:/api/post/detail" + pid;
    }
}
