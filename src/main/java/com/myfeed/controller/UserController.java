package com.myfeed.controller;

import com.myfeed.model.post.Post;
import com.myfeed.model.user.LoginProvider;
import com.myfeed.model.user.RegisterDto;
import com.myfeed.model.user.UpdateDto;
import com.myfeed.model.user.User;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.MacSpi;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

//todo return 전체 조율 후 수정 필요
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
        String hashedPwd = BCrypt.hashpw(registerDto.getPwd(), BCrypt.gensalt());
        User user = User.builder()
                .email(registerDto.getEmail()).password(hashedPwd)
                .username(registerDto.getUname()).nickname(registerDto.getNickname())
                .profileImage(registerDto.getProfileImage())
                .phoneNumber(registerDto.getPhoneNumber())
                .loginProvider(LoginProvider.FORM)
                .build();
        userService.registerUser(user);
        messagemap.put("success","회원가입 되었습니다.");
        messagemap.put("redirectUrl","/home");
        return ResponseEntity.ok(messagemap);
    }

    @GetMapping("/update/{uid}")
    public String update() {
        return "user/update";
    }

    // 사용자 정보 수정
    @PostMapping("/{uid}") // 변경 가능 필드(비밀번호, 실명, 닉네임, 프로필사진)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProc(@PathVariable Long id,
            @Validated @RequestBody UpdateDto updateDto) {
        Map<String, Object> messagemap = new HashMap<>();
        userService.updateUser(id, updateDto);
        messagemap.put("success","회원정보가 수정되었습니다.");
        String redirectUrl = "/"+id+"/detail";
        messagemap.put("redirectUrl",redirectUrl);
        return ResponseEntity.ok(messagemap);
    }

    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUserExist(@RequestParam(name="email") String email) {
        Map<String, Object> messagemap = new HashMap<>();
        if (userService.findByEmail(email) != null) {
            messagemap.put("state", "error");
            messagemap.put("message", "이미 회원가입된 이메일입니다.");
            return ResponseEntity.badRequest().body(messagemap);
        }
        messagemap.put("state", "success");
        messagemap.put("message", "이메일을 사용할 수 있습니다.");
        return ResponseEntity.ok().body(messagemap);
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
    @GetMapping("/{id}/detail")
    public String detail(@PathVariable Long id,
            @RequestParam(name="p", defaultValue = "1") int page,
            Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        Page<Post> postList = postService.getMyPostList(page, id);
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
    @PostMapping("{uid}/status")
    public String updateUserState(@PathVariable Long id,
                                    @RequestParam(name="status") boolean status,
                                    Model model) {
        userService.updateUserStatus(id, status);
        //todo
        return "redirect:/user/list";
    }
}
