package com.myfeed.controller;

import com.myfeed.model.post.Post;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/post")
public class PostController {
    @Autowired PostService postService;

    @GetMapping("/create")
    public String CreatePostForm() {
        return "user/create";
    }

    @PostMapping("/create")
    public String createPostProc(long id, String category, String title, String content, String imgSrc) {
        postService.createPost(id, category, title, content, imgSrc);
        return "redirect:/api/postEs/list";
    }

    @GetMapping("/myList")
    public  String myList(@PathVariable long uid, Model model) {
        List<Post> myPostList = postService.getMyPostList(uid);
        // 보류
        model.addAttribute("myPostList", myPostList);
        return "api/post/myList";
    }

    @GetMapping("detail/{pid}")
    public String detail(@PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        // 보류
        model.addAttribute("post", post);
        return "api/post/detail";
    }

    // listAll(관리자) - 신고된 글 list(enum)


    @GetMapping("/update/{pid}")
    public String updatePostForm(@PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        model.addAttribute("post", post);
        return "api/post/update";
    }


    @PostMapping("/update")
    public String updatePostProc(@RequestBody Post post) {
        Post updatedPost = postService.findByPid(post.getPid());

        updatedPost.setTitle(post.getTitle());
        updatedPost.setContent(post.getContent());
        updatedPost.setImgSrc(post.getImgSrc());
        updatedPost.setUpdateAt(LocalDateTime.now());

        postService.updatePost(updatedPost);
        return "redirect:/api/post/detail";
    }

    @GetMapping("/delete/{pid}")
    public String delete(@PathVariable long pid) {
        postService.deletePost(pid);
        return "redirect:/api/post/myList";
    }
}
