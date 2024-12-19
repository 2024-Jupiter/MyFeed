package com.myfeed.controller;

import com.myfeed.model.user.User;
import com.myfeed.service.user.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


// todo /loginsuccess, failure
@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired UserService userService;

    // 회원 가입
    @GetMapping("/register")
    public String registerForm(){
        return "user/register";
    }

    @PostMapping("/register")
    public String registerProc(String uid, String pwd,String pwd2, String uname, String nickname, String email, String profileImage){
        if (userService.findByUid(uid) == null && pwd.equals(pwd2) && pwd.length() >= 4){
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            User user = new User(uid, hashedPwd, uname, nickname, email, profileImage);
            userService.registerUser(user);
        }
        return "redirect:/api/board/list";
    }

    // 회원 탈퇴
    @GetMapping("/{uid}")
    public String delete(@PathVariable String uid) {
        userService.deleteUser(uid);
        return "redirect:/user/list";
    }

    // 회원정보 상세보기
    @GetMapping("/detail/{uid}")
    public String detail(@PathVariable String uid, Model model){
        User user = userService.findByUid(uid);
        model.addAttribute("user", user);
        return "user/detail";
    }

}
