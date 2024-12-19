package com.myfeed.controller;

import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostCommentList;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.comment.CommentService;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/post")
public class PostController {
    @Autowired PostService postService;
    @Autowired CommentService commentService;
    @Autowired UserService userService;

    @GetMapping("/create")
    public String CreatePostForm() {
        return "user/create";
    }

    @PostMapping("/create")
    public String createPostProc(String uid, String category, String title, String content, String imgSrc) {
        postService.createPost(uid, category, title, content, imgSrc);
        return "redirect:/api/postEs/list";
    }

    @GetMapping("/update/{pid}")
    public String updatePostForm(@PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        model.addAttribute("post", post);
        return "api/post/update";
    }

    /*
    @PostMapping("/update")
    public String updatePostProc(@RequestBody Post post) {
        Post updatedPost = postService.findByPid(post.getPid());
        User user = userService.findByUid(post.getUser().getUid());

        updatedPost.setUser(user);
        updatedPost.setCategory(post.getCategory());
        updatedPost.setTitle(post.getTitle());
        updatedPost.setContent(post.getContent());

    }

     */
}
