package com.myfeed.controller;

import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Category;
import com.myfeed.model.post.Image;
import com.myfeed.model.post.Post;;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ReportType;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.reply.ReplyService;
import com.myfeed.service.report.ReportService;
import jakarta.servlet.http.HttpSession;
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
    @Autowired ReplyService replyService;
    @Autowired ReportService reportService;

    // 게시글 작성
    @GetMapping("/create")
    public String CreatePostForm() {
        return "api/post/create";
    }
    // 게시글 작성
    @PostMapping("/create")
    public String createPostProc(@PathVariable long uid, @RequestParam Category category,
                                 @RequestParam String title, @RequestParam String content, @RequestParam String imgSrc) {
        postService.createPost(uid, category, title, content, imgSrc);
        return "redirect:/api/postEs/list";
    }

    // 내 게시글 페이지네이션
    @GetMapping("/myList/{uid}")
    public  String myList(@RequestParam(name="p", defaultValue = "1") int page,
                          @PathVariable long uid, HttpSession session, Model model) {
        Page<Post> myPostPage = postService.getMyPostList(page, uid);

        List<Post> filteredMyList = new ArrayList<>();
        User user = postService.getByUserUid(uid);
        for (Post post: filteredMyList) {
            if (user.isDeleted()) {
                filteredMyList.add(post);
            }
            if (post.getStatus() == BlockStatus.NORMAL_STATUS) {
                filteredMyList.add(post);
            } else {
                Post blockedPostPlaceholder = new Post();
                blockedPostPlaceholder.setTitle("차단된 게시글입니다.");
                filteredMyList.add(blockedPostPlaceholder);
            }
        }

        int totalPages = myPostPage.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / reportService.PAGE_SIZE - 1) * reportService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + reportService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Post> filteredPagedResult = new PageImpl<>(filteredMyList, pageable, myPostPage.getTotalElements());

        session.setAttribute("currentMyPostPage", page);
        model.addAttribute("myPostList", filteredPagedResult.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "api/post/myList" + uid;
    }

    // 게시글 상페보기 - 댓글 페이지네이션
    @GetMapping("detail/{pid}")
    public String detail(@RequestParam(name="p", defaultValue = "1") int page, @PathVariable long pid,
                         @RequestParam(name = "likeAction", required = false) String likeAction,
                         HttpSession session, Model model) {
        Post post = postService.findByPid(pid);
        User user = post.getUser();
        List<Image> imgaeList = post.getImages();

        postService.incrementViewCount(pid);
        if ("like".equals(likeAction)) {
            postService.incrementLikeCount(pid);
        } else {
            postService.decrementLikeCount(pid);
        }

        Page<Reply> pagedResult = replyService.getPageByPostContaining(page, post);

        List<Reply> filteredReplyList = new ArrayList<>();
        for (Reply reply : pagedResult.getContent()) {
            if (user.isDeleted()) {
                filteredReplyList.add(reply);
            }
            if (reply.getStatus() == BlockStatus.NORMAL_STATUS) {
                filteredReplyList.add(reply);
            } else {
                Reply blockedReplyPlaceholder = new Reply();
                blockedReplyPlaceholder.setContent("차단된 댓글입니다.");
                filteredReplyList.add(blockedReplyPlaceholder);
            }
        }

        int totalPages = pagedResult.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / reportService.PAGE_SIZE - 1) * reportService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + reportService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Reply> filteredPagedResult = new PageImpl<>(filteredReplyList, pageable, pagedResult.getTotalElements());

        session.setAttribute("currentPostPage", page);
        model.addAttribute("imageList", imgaeList);
        model.addAttribute("postReplyList", filteredPagedResult.getContent());
        model.addAttribute("viewCount", post.getViewCount());
        model.addAttribute("likeCount", post.getLikeCount());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "api/post/detail/" + pid;
    }

    // 게시글 수정
    @GetMapping("/update/{pid}")
    public String updatePostForm(@PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        model.addAttribute("post", post);
        return "api/post/update";
    }
    // 게시글 수정
    @PreAuthorize("#user.id == authentication.principal.id")
    @PostMapping("/update")
    public String updatePostProc(@RequestBody Post post) {
        Post updatedPost = postService.findByPid(post.getId());

        updatedPost.setTitle(post.getTitle());
        updatedPost.setContent(post.getContent());
        updatedPost.setImages(post.getImages());

        postService.updatePost(updatedPost);
        return "redirect:/api/post/detail/" + post.getId();
    }

    // 게시글 수정
    @PreAuthorize("#user.id == authentication.principal.id")
    @GetMapping("/delete/{pid}")
    public String delete(@PathVariable long pid) {
        postService.deletePost(pid);
        return "redirect:/api/post/myList";
    }

    // 신고
    @GetMapping("/report/{pid}")
    public String saveReportForm() {
        return "api/report/save";
    }
    // 신고
    @PostMapping("/report")
    public String saveReportProc(ReportType reportType,
                                 @RequestParam long pid, @RequestParam(required = false) String description) {
        reportService.reportPost(reportType, pid, description);
        return "redirect:/api/post/detail/" + pid;
    }
}
