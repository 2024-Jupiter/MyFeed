package com.myfeed.controller;

import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Image;
import com.myfeed.model.post.Post;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostEsService;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.Post.PostServiceImpl;
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
@RequestMapping("/api/postEs")
public class PostEsController {

    /*
    // 게시글 상세 보기
    @GetMapping("detail/{pid}")
    public String detail(@RequestParam(name="p", defaultValue = "1") int page, @PathVariable long id,
                         @RequestParam(name = "likeAction", required = false) String likeAction,
                         HttpSession session, Model model) {
        Post post = postService.findPostById(id);
        User user = post.getUser();
        List<Image> imgaeList = post.getImages();

        // 조회수 증가 (동시성)
        postService.incrementPostViewCountById(id);
        if ("like".equals(likeAction)) {
            // 좋아요 증가 (동시성)
            postService.incrementPostLikeCountById(id);
        } else {
            // 좋아요 감소 (동시성)
            postService.decrementPostLikeCountById(id);
        }

        Page<Reply> pagedResult = replyService.getPagedRepliesByPost(page, post);

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
        return "api/post/detail/" + id;
    }
     */
}
