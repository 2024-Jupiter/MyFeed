package com.myfeed.controller;

import com.myfeed.model.post.Post;
import com.myfeed.model.user.LoginProvider;
import com.myfeed.model.user.RegisterDto;
import com.myfeed.model.user.Role;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired UserService userService;
    @Autowired PostService postService;

    // 회원 가입(폼)
    @GetMapping("/register")
    public String registerForm(){
        return "user/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registerProc(@Validated @RequestBody RegisterDto registerDto){
        Map<String, Object> messagemap = new HashMap<>();
        // todo
        //전화번호 3 부분으로 쪼개기
        // pwd, pwd2 동일 검증 로직
        // pwd 조건 만족하는지 검증 로직 만들기
        // 유효한 이메일, 휴대전화 인증 로직
        String hashedPwd = BCrypt.hashpw(registerDto.getPwd(), BCrypt.gensalt());
        User user = User.builder()
                .email(registerDto.getEmail()).password(hashedPwd)
                .username(registerDto.getUname()).nickname(registerDto.getNickname())
                .role(Role.USER).isActive(true)
                .profileImage(registerDto.getProfileImage())
                .phoneNumber(registerDto.getPhoneNumber())
                .loginProvider(LoginProvider.FORM)
                .createdAt(LocalDateTime.now())
                .build();
        userService.registerUser(user);

        return ResponseEntity.ok(messagemap);
    }

    // 회원 탈퇴
    @GetMapping("/{id}")
    public String delete(@PathVariable Long id) {
        User user = userService.findById(id);
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        return "redirect:/user/list";
    }

    // 회원정보 상세보기
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        List<Post> postList = postService.getMyPostList(id);
        model.addAttribute("postList", postList);
        return "user/detail";
    }

    // 로그인
    @GetMapping("/login")
    @ResponseBody
    public String loginForm() {
        return "<h1>hello<h1>";
        //return "user/login"
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

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/board/list;";
    }

    // 사용자 정보 수정 // todo DTO로 받아오기
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
        user.setUpdatedAt(LocalDateTime.now());
        userService.updateUser(user);
        return "redirect:/board/list";
    }

    // 활성/비활성 회원 목록 가져오기
    @GetMapping("/list")
    public String list(@RequestParam(name="p", defaultValue = "1") int page,
                        @RequestParam(name="status", defaultValue = "true") boolean status,
                        Model model) {
        Page<User> pagedUsers = userService.getPagedUser(page, status);
        model.addAttribute("pagedUsers", pagedUsers);
        model.addAttribute("status", status);
        model.addAttribute("currentUserPage", page);
        return "user/list";
    }

    //회원 활성/비활성 여부 수정하기
    @PostMapping("/status/{uid}")
    public String updateUserState(@PathVariable Long id,
                                    @RequestParam(name="status") boolean status,
                                    Model model) {
        User user = userService.findById(id);
        if (user.isActive() != status) {
            user.setActive(status);
            userService.updateUser(user);
        }
        return "redirect:/user/list";
    }
}
