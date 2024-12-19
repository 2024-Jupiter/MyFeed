package com.myfeed.controller;

import com.myfeed.model.user.User;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired UserService userService;
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

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

    // 로그인
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    // 로그인 성공 시
    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        User user = userService.findByUid(uid);
        session.setAttribute("sessUid", uid);
        session.setAttribute("sessUname", user.getUname());
        String msg = user.getUname() + "님 환영합니다.";
        String url = "/";
        model.addAttribute("msg", msg);
        model.addAttribute("url", url); 
        return "common/alertMsg";
    }



}
