package com.myfeed.controller;

import com.myfeed.model.post.Post;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/post")
public class PostController {
    @Autowired PostService postService;
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
}
