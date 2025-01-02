package com.myfeed.controller;

import com.myfeed.model.post.*;
;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.reply.ReplyDetailDto;
import com.myfeed.model.reply.ReplyDto;
import com.myfeed.model.report.ReportDto;
import com.myfeed.model.report.ReportType;
import com.myfeed.model.user.Role;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.reply.ReplyService;
import com.myfeed.service.report.ReportService;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.myfeed.service.reply.ReplyService.PAGE_SIZE;

@Controller
@RequestMapping("/api/posts")
public class PostController {
    @Autowired PostService postService;
    @Autowired UserService userService;
    @Autowired ReplyService replyService;

    // 게시글 작성 폼 (GET 요청 으로 폼을 가져옴)
    @GetMapping("create")
    public String createPostForm() {
        return "api/posts/create";
    }

    // 게시글 작성
    @ResponseBody
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> createPost(@RequestParam Long userId,
                                                          @Valid @RequestBody PostDto postDto) {
        Post post = postService.createPost(userId, postDto);
        Map<String, Object> response = new HashMap<>();

        String redirectUrl = "/api/posts/detail/" + post.getId();
        response.put("redirectUrl",redirectUrl);
        response.put("success", true);
        response.put("message", "게시글이 작성 되었습니다.");
        response.put("data", post);

        return ResponseEntity.ok(response);
    }

    // 내 게시글 페이지 네이션
    @ResponseBody
    @GetMapping("/user/{userId}")
    @PreAuthorize("#user.id == authentication.principal.id")
    public ResponseEntity<Map<String, Object>> myPostList(@RequestParam(name="p", defaultValue = "1") int page,
                                                           @PathVariable long userId, HttpSession session) {
        User user = userService.findById(userId);
        Page<Post> posts = postService.getPagedPostsByUserId(page, user);
        Map<String, Object> response = new HashMap<>();

        posts.getContent().forEach(post -> {
            if (post.getStatus() == BlockStatus.BLOCK_STATUS) {
                post.setContent("차단된 게시글 입니다.");
            }
        });

        int totalPages = posts.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / postService.PAGE_SIZE - 1) * postService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + postService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageList.add(i);
        }

        session.setAttribute("currentPostPage", page);
        response.put("success", true);
        response.put("message", "내 게시글 리스트");
        response.put("data", posts.getContent());
        response.put("totalPages", totalPages);
        response.put("startPage", startPage);
        response.put("endPage", endPage);
        response.put("pageList", pageList);

        return ResponseEntity.ok(response);
    }

    // 게시글 상세 보기(댓글 페이지 네이션 & 조회수 증가 & 좋아요 기능)
    @ResponseBody
    @GetMapping("detail/{postId}")
    public ResponseEntity<Map<String, Object>> detail(@RequestParam(name="p", defaultValue = "1") int page, @PathVariable long id,
                                                      @RequestParam(name = "likeAction", required = false) String likeAction,
                                                      HttpSession session) {
        Post post = postService.findPostById(id);
        Map<String, Object> response = new HashMap<>();

        // 조회수 증가 (동시성)
        postService.incrementPostViewCountById(id);
        if ("like".equals(likeAction)) {
            // 좋아요 증가 (동시성)
            postService.incrementPostLikeCountById(id);
        } else {
            // 좋아요 감소 (동시성)
            postService.decrementPostLikeCountById(id);
        }

        PostDetailDto postDetailDto = new PostDetailDto(post);

        Page<Reply> replies = replyService.getPagedRepliesByPost(page, post);
        replies.getContent().forEach(reply -> {
            if (reply.getStatus() == BlockStatus.BLOCK_STATUS) {
                reply.setContent("차단된 댓글 입니다.");
            }
        });

        List<ReplyDetailDto> replyDetailDto = replies.getContent().stream()
                .map(ReplyDetailDto::new)
                .toList();

        int totalPages = replies.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / postService.PAGE_SIZE - 1) * postService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + postService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("currentPostPage", page);
        String redirectUrl = "/api/posts/detail/" + post.getId();
        response.put("redirectUrl",redirectUrl);
        response.put("success", true);
        response.put("message", "게시글 상세 보기");
        response.put("post", postDetailDto);
        response.put("replies", replyDetailDto);
        response.put("totalPages", totalPages);
        response.put("startPage", startPage);
        response.put("endPage", endPage);
        response.put("pageList", pageList);

        return ResponseEntity.ok(response);
    }

    // 게시글 수정
    @ResponseBody
    @PatchMapping("/{id}")
    @PreAuthorize("#user.id == authentication.principal.id")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateDto updateDto) {
        Post post = postService.updatePost(id, updateDto);
        Map<String, Object> response = new HashMap<>();

        String redirectUrl = "/api/posts/detail/" + post.getId();
        response.put("redirectUrl",redirectUrl);
        response.put("success", true);
        response.put("message", "게시글이 수정 되었습니다.");
        response.put("data", post);

        return ResponseEntity.ok(response);
    }

    // 게시글 삭제
    @ResponseBody
    @DeleteMapping("/{id}")
    @PreAuthorize("#user.id == authentication.principal.id")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long id) {
        Post post = postService.findPostById(id);
        postService.deletePostById(id);
        Map<String, Object> response = new HashMap<>();

        String redirectUrl = "/api/posts/detail/" + id;
        response.put("redirectUrl", redirectUrl);
        response.put("success", true);
        response.put("message", "댓글이 삭제 되었습니다.");
        response.put("data", post);

        return ResponseEntity.ok(response);
    }

    // 게시글 조회수 증가
    @ResponseBody
    @PostMapping("/{id}/view")
    public ResponseEntity<Map<String, Object>> incrementPostViewCount(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            postService.incrementPostViewCountById(id);
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Post not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 게시글 좋아요 증가
    @ResponseBody
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> incrementPostLikeCount(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            postService.incrementPostLikeCountById(id);
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Post not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 게시글 좋아요 감소
    @ResponseBody
    @PostMapping("/{id}/unLike")
    public ResponseEntity<Map<String, Object>> decrementPostLikeCount(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            postService.decrementPostLikeCountById(id);
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Post not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
