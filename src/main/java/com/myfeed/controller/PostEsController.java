package com.myfeed.controller;

import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.service.Post.PostEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.myfeed.service.Post.CsvFileReaderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/search")
public class PostEsController {
    @Autowired CsvFileReaderService csvFileReaderService;
    @Autowired PostEsService postEsService;

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
    // 일렉스틱 서치에 크롤링 데이터 저장
    @GetMapping("/Elasticsearch")
    @ResponseBody
    public String Elasticsearch() {
        csvFileReaderService.csvFileToElasticSearch();
        return "<h1>일래스틱 서치에 데이터를 저장 했습니다.</h1>";
    }

    // 게시글 리스트


    // 게시글 상세 보기


}
