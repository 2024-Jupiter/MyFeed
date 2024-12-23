package com.myfeed.controller;

import com.myfeed.model.board.Board;
import com.myfeed.model.board.Tag;
import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostReplyList;
import com.myfeed.model.reply.Reply;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.board.BoardService;
import com.myfeed.service.reply.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static com.myfeed.service.reply.ReplyService.PAGE_SIZE;

@Controller
@RequestMapping("/api/board")
public class BoardController {
    @Autowired BoardService boardService;
    @Autowired PostService postService;
    @Autowired ReplyService replyService;

    // 차단 된 댓글 안보이게
    @GetMapping("/list")
    public String getPagedBoardByCategory(@RequestParam(name="p", defaultValue = "1") int page, @RequestParam("tag") Tag tag, Model model) {
        Page<Board> boardPage = boardService.getPagedBoardByCategory(page, tag);

        List<Board> filteredBoardList = new ArrayList<>();
        for (Board board: boardPage.getContent()) {
            if (board.getPost().getBlockStatus() == BlockStatus.NORMAL_STATUS) {
                filteredBoardList.add(board);
            } else {
                model.addAttribute("message", "차단된 게시글입니다.");
            }
        }

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Board> filteredPagedResult = new PageImpl<>(filteredBoardList, pageable, boardPage.getTotalElements());
        model.addAttribute("boardPage", filteredPagedResult);
        return "api/board/list";
    }

    @GetMapping("detail/{pid}")
    public String detail(@RequestParam(name="p", defaultValue = "1") int page, @PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        Page<Reply> pagedResult = replyService.getPageByPostContaining(page, post);

        List<Reply> filteredReplyList = new ArrayList<>();
        for (Reply reply : pagedResult.getContent()) {
            if (reply.getBlockStatus() == BlockStatus.NORMAL_STATUS) {
                filteredReplyList.add(reply);
            } else {
                model.addAttribute("message", "차단된 댓글입니다.");
            }
        }

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Reply> filteredPagedResult = new PageImpl<>(filteredReplyList, pageable, pagedResult.getTotalElements());
        model.addAttribute("post", post);
        model.addAttribute("pagedResult", filteredPagedResult);
        return "api/post/detail/" + pid;
    }
}
