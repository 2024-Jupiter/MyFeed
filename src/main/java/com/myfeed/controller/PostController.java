package com.myfeed.controller;

import com.myfeed.model.board.Tag;
import com.myfeed.model.post.Post;;
import com.myfeed.model.post.PostReplyList;
import com.myfeed.model.reply.Reply;
import com.myfeed.service.Post.PostReplyListService;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.reply.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/post")
public class PostController {
    @Autowired PostService postService;
    @Autowired PostReplyListService postReplyListService;
    @Autowired ReplyService replyService;

    @GetMapping("/create")
    public String CreatePostForm() {
        return "api/post/create";
    }

    @PostMapping("/create")
    public String createPostProc(@PathVariable long uid, Tag tag, String title, String content, String imgSrc) {
        postService.createPost(uid, tag, title, content, imgSrc);
        return "redirect:/api/postEs/list";
    }

    @GetMapping("/myList")
    public  String myList(@PathVariable long uid, Model model) {
        List<Post> myPostList = postService.getMyPostList(uid);
        model.addAttribute("myPostList", myPostList);
        return "api/post/myList";
    }

    @GetMapping("detail/{pid}")
    public String detail(@PathVariable long pid, Model model) {
        Post post = postService.findByPid(pid);
        List<PostReplyList> postReplyLists = postReplyListService.findByPostPid(pid);
        for (PostReplyList lists: postReplyLists) {
            long rid = lists.getReply().getRid();
            Reply reply = replyService.findByRid(rid);
            model.addAttribute("reply", reply);
        }
        model.addAttribute("post", post);
        return "api/post/detail/" + pid;
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
        return "redirect:/api/post/detail/" + post.getPid();
    }

    @GetMapping("/delete/{pid}")
    public String delete(@PathVariable long pid) {
        postService.deletePost(pid);
        return "redirect:/api/post/myList";
    }
}
