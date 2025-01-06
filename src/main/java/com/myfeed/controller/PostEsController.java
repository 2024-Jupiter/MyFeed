package com.myfeed.controller;

import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.service.Post.PostEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/search")
public class PostEsController {


    @Autowired
    private PostEsService postEsService;

    @GetMapping()
    @ResponseBody
    public Page<PostEs> search(@RequestParam(name = "q") String query,
                               @RequestParam(name = "p", defaultValue = "1") int page,
                               Model model) {
        Page<PostEs> posts = postEsService.searchQuery(query,page);
//        model.addAttribute("posts", posts);
        return posts;
    }

    @GetMapping("/jsonInsert")
    @ResponseBody
    public String jsonInsert() {
        postEsService.insertByJsonFile();
        return "Json Data Inserted";
    }

}
