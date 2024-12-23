package com.myfeed.controller;

import com.myfeed.model.board.Board;
import com.myfeed.model.board.Tag;
import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostReplyList;
import com.myfeed.model.reply.Reply;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.board.BoardService;
import com.myfeed.service.reply.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/board")
public class BoardController {
    @Autowired BoardService boardService;
    @Autowired PostService postService;
    @Autowired ReplyService replyService;

    @GetMapping("/boards")
    public Page<Board> getPagedBoardByCategory(@RequestParam(name="p", defaultValue = "1") int page, @RequestParam("tag") Tag tag) {
        return boardService.getPagedBoardByCategory(page, tag);
    }

    @GetMapping("detail/{pid}")
    public String detail(@RequestParam(name="p", defaultValue = "1") int page, @PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        Page<Reply> pagedResult = replyService.getPageByPostContaining(page, post);
        model.addAttribute("post", post);
        model.addAttribute("pagedResult", pagedResult);
        return "api/post/detail/" + pid;
    }
}
