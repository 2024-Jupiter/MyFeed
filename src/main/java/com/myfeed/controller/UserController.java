package com.myfeed.controller;

import com.myfeed.model.user.LoginProvider;
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

    // 회원 가입(폼)
    @GetMapping("/register")
    public String registerForm(){
        return "user/register";
    }

    @PostMapping("/register")
    public String registerProc(String email, String pwd,String pwd2, String uname, String nickname, String profileImage){
        if (userService.findByEmail(email) == null && pwd.equals(pwd2) && pwd.length() >= 4){
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            User user = new User(email, hashedPwd, uname, nickname, profileImage, LoginProvider.FORM);
            userService.registerUser(user);
        }
        return "redirect:/api/board/list";
    }

    // 회원 탈퇴
    @GetMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user/list";
    }

    // 회원정보 상세보기
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        User user = userService.findById(id);
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
        String email = authentication.getName();
        User user = userService.findByEmail(email);


        session.setAttribute("sessId", user.getId());
        String msg = user.getNickname() + "님 환영합니다.";
        String url = "/";
        model.addAttribute("msg", msg);
        model.addAttribute("url", url); 
        return "common/alertMsg";
    }

    // 사용자 정보 수정
    @PostMapping("/update")
    public String updateProc(String email, String pwd,String pwd2, String username, String nickname, String profileImage) {
        User user = userService.findByEmail(email);
        if (pwd.equals(pwd2) && pwd.length()>=4){
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            user.setPassword(hashedPwd);
        }
        user.setUsername(username);
        user.setNickname(nickname);
        user.setProfileImage(profileImage);
        userService.updateUser(user);
        return "redirect:/board/list";
    }
}
