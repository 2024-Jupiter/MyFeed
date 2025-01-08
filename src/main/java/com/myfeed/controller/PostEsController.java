package com.myfeed.controller;

import com.myfeed.service.Post.CsvFileReaderService;
import com.myfeed.service.Post.PostEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/postEs")
public class PostEsController {
    @Autowired CsvFileReaderService csvFileReaderService;
    @Autowired PostEsService postEsService;

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
