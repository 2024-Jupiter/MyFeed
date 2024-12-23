package com.myfeed.controller;

import com.myfeed.ascept.CheckPermission;
import com.myfeed.model.board.Tag;
import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Post;;
import com.myfeed.model.post.PostReplyList;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ReportType;
import com.myfeed.service.Post.PostReplyListService;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.reply.ReplyService;
import com.myfeed.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.myfeed.service.reply.ReplyService.PAGE_SIZE;

@Controller
@RequestMapping("/api/post")
public class PostController {
    @Autowired PostService postService;
    @Autowired PostReplyListService postReplyListService;
    @Autowired ReplyService replyService;
    @Autowired ReportService reportService;

    @GetMapping("/create")
    public String CreatePostForm() {
        return "api/post/create";
    }

    @PostMapping("/create")
    public String createPostProc(@PathVariable long uid, Tag tag, String title, String content, String imgSrc) {
        postService.createPost(uid, tag, title, content, imgSrc);
        return "redirect:/api/postEs/list";
    }

    // 차단 된 게시글 안보이게
    @GetMapping("/myList")
    public  String myList(@PathVariable long uid, Model model) {
        List<Post> myPostList = postService.getMyPostList(uid);
        for (Post post: myPostList) {
            if (post.getBlockStatus() == BlockStatus.NORMAL_STATUS) {
                model.addAttribute("myPostList", myPostList);
            } else {
                model.addAttribute("message", "차단된 게시글입니다.");
                return "api/post/myList";
            }
        }
        return "api/post/myList";
    }

    @GetMapping("detail/{pid}")
    public String detail(@RequestParam(name="p", defaultValue = "1") int page, @PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        Page<Reply> pagedResult = replyService.getPageByPostContaining(page, post);

        List<Reply> filteredReplyList = new ArrayList<>();
        for (Reply reply : pagedResult.getContent()) {
            if (reply.getBlockStatus() == BlockStatus.NORMAL_STATUS) {
                filteredReplyList.add(reply);
            } else {
                model.addAttribute("message", "차단된 댓글입니다.");
            }
        }

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Reply> filteredPagedResult = new PageImpl<>(filteredReplyList, pageable, pagedResult.getTotalElements());
        model.addAttribute("post", post);
        model.addAttribute("pagedResult", filteredPagedResult);
        return "api/post/detail/" + pid;
    }

    @GetMapping("/update/{pid}")
    public String updatePostForm(@PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        model.addAttribute("post", post);
        return "api/post/update";
    }

    @PreAuthorize("#user.id == authentication.principal.id")
    @PostMapping("/update")
    public String updatePostProc(@RequestBody Post post) {
        Post updatedPost = postService.findByPid(post.getPid());

        updatedPost.setTitle(post.getTitle());
        updatedPost.setContent(post.getContent());
        updatedPost.setImgSrc(post.getImgSrc());
        updatedPost.setUpdateAt(LocalDateTime.now());

        postService.updatePost(updatedPost);
        return "redirect:/api/post/detail/" + post.getPid();
    }

    @PreAuthorize("#user.id == authentication.principal.id")
    @GetMapping("/delete/{pid}")
    public String delete(@PathVariable long pid) {
        postService.deletePost(pid);
        return "redirect:/api/post/myList";
    }

    @GetMapping("/report/{pid}")
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
