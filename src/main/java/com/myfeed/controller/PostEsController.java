package com.myfeed.controller;

import com.myfeed.model.elastic.PostEsDto1;
import com.myfeed.model.elastic.SearchField;
import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.service.Post.EsLogService;
import com.myfeed.service.Post.PostEsService;
import com.myfeed.service.Post.PostServiceImpl;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import com.myfeed.service.Post.CsvFileReaderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/search/posts")
public class PostEsController {
    @Autowired CsvFileReaderService csvFileReaderService;
    @Autowired PostEsService postEsService;
    @Autowired
    private EsLogService esLogService;


    // 기본 검색 ( 제목, 내용, 제목+내용 )
    @GetMapping
    @ResponseBody
    public Page<PostEs> searchPosts(
        @RequestParam String q,
        @RequestParam(name = "p", defaultValue = "1") int page,
        @RequestParam(name = "userId", required = false) String userId,
        @RequestParam(name = "field", defaultValue = "TITLE") SearchField field
    ) throws IOException {
//        esLogService.saveSearchLog(userId, q);
        return postEsService.searchGeneralPosts(q,field, page);
    }

    // 사용자 검색어 상위 3위 게시글 추천
    @GetMapping("recommend")
    @ResponseBody
    public String recommend(@RequestParam(name="p", defaultValue = "1") int page, HttpSession session, Model model,@RequestParam(name = "userId") String userId)
        throws IOException {
//        var pagedResult = postEsService.getRecommendedPostsBySearchLog(page,"1");
//        var pagedResult = postEsService.getRecommendPostForMe(page,userId);
        var pagedResult = postEsService.getRecommendPostByTop3Keywords(page);
//        System.out.println("pagedResult: " + pagedResult);
        List<PostEsDto1> postList = pagedResult.getContent();
        int totalPages = pagedResult.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / PostServiceImpl.PAGE_SIZE - 1) * PostServiceImpl.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + PostServiceImpl.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("currentPostPage", page);
        model.addAttribute("postList", postList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "api/search/posts/recommend";
    }

    // 비슷한 게시물 추천 - postId가 음수일 경우 작동 안함 ㅠ
    @GetMapping("/recommend/{postId}/similar")
    @ResponseBody
    public Page<PostEsDto1> getRecommendationsByPostId(
        @PathVariable String postId,
        @PageableDefault(size = 10) Pageable pageable
    ) throws IOException {
        return postEsService.findSimilarPostsById(postId, pageable);
    }
    // 비슷한 게시물 추천 - keywords로 검색
    @GetMapping("/recommend/keywords")
    @ResponseBody
    public Page<PostEsDto1> getRecommendationsByKeywords(
        @RequestParam List<String> keywords,
        @PageableDefault(size = 10) Pageable pageable
    ) throws IOException {
        System.out.println("keywords: " + keywords);
        return postEsService.findSimilarPostsByKeywords(keywords, pageable);
    }
    // 일렉스틱 서치에 크롤링 데이터 저장
    @GetMapping("/Elasticsearch")
    @ResponseBody
    public String Elasticsearch() {
        csvFileReaderService.csvFileToElasticSearch();
        return "<h1>일래스틱 서치에 데이터를 저장 했습니다.</h1>";
    }
    @GetMapping("init/news")
    @ResponseBody
    public String ElasticsearchNewsInit() {
//        postEsService.initNewsData();
        return "<h1>일래스틱 서치에 뉴스 데이터를 저장 했습니다.</h1>";
    }
    @GetMapping("/init/velog")
    @ResponseBody
    public String ElasticsearchPostsInit() {
//        postEsService.initVelogData();
        return "<h1>일래스틱 서치에 velog 데이터를 저장 했습니다.</h1>";
    }

}
