package com.myfeed.controller;

import com.myfeed.model.post.Post;
import com.myfeed.service.Post.PostEsService;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.Post.PostServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/search")
public class PostEsController {

    @Autowired
    private PostEsService postEsService;

    @GetMapping()
    public String search(@RequestParam(name = "q") String query,
                         @RequestParam(name = "p", defaultValue = "1") int page,
                         Model model) {
        Page<Post> posts = postEsService.searchQuery(query,page);
        model.addAttribute("posts", posts);
        return "search";
    }

}
