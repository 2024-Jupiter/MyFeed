package com.myfeed.controller;

import com.myfeed.model.board.Board;
import com.myfeed.model.board.Tag;
import com.myfeed.service.board.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("/api/board")
public class BoardController {
    @Autowired BoardService boardService;

    @GetMapping("/boards")
    public Page<Board> getPagedBoardByCategory(@RequestParam("page") int page, @RequestParam("tag") Tag tag) {
        return boardService.getPagedBoardByCategory(page, tag);
    }
}
