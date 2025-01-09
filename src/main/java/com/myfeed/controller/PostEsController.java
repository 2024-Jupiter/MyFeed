package com.myfeed.controller;

import com.myfeed.service.Post.PostEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/postEs")
public class PostEsController {
    @Autowired PostEsService postEsService;

    // 게시글 리스트


    // 게시글 상세 보기


}
