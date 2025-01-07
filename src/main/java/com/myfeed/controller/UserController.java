
package com.myfeed.controller;

import com.myfeed.exception.CustomException;
import com.myfeed.exception.ExpectedException;
import com.myfeed.model.post.Post;
import com.myfeed.model.user.LoginProvider;
import com.myfeed.model.user.RegisterDto;
import com.myfeed.model.user.Role;
import com.myfeed.model.user.UpdateDto;
import com.myfeed.model.user.User;
import com.myfeed.model.user.UserChangePasswordDto;
import com.myfeed.model.user.UserFindIdDto;
import com.myfeed.model.user.UserFindPasswordDto;
import com.myfeed.response.ErrorCode;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api/users")
public class UserController {
    @Autowired UserService userService;
    @Autowired PostService postService;

    @GetMapping("/test")
    public String testEndpoint(@CookieValue(name = "accessToken", required = false) String accessToken) {
        System.out.println("accessToken: " + accessToken);
        return "main";
    }

    // 로그인
    @GetMapping("/custom-login")
    public String loginForm() {
        Map<String, Object> messagemap = new HashMap<>();
        return "main";
    }

    // 회원 가입(폼)
    @GetMapping("/register")
    public String registerForm(){
        return "users/register";
    }

    @PostMapping("/register") // todo 휴대폰 번호 인증 시 DTO 값 null
    public String registerProc(@Validated RegisterDto registerDto, Model model){
        Map<String, Object> messagemap = new HashMap<>();
        String hashedPwd = BCrypt.hashpw(registerDto.getPwd(), BCrypt.gensalt());
//        if (registerDto.getEmail().equals("asd@naver.com")) {
//            throw new ExpectedException(ErrorCode.USER_NOT_FOUND);
//        }
        User user = User.builder()
                .email(registerDto.getEmail()).password(hashedPwd)
                .username(registerDto.getUname()).nickname(registerDto.getNickname())
                .profileImage(registerDto.getProfileImage())
                .phoneNumber(registerDto.getPhoneNumber())
                .loginProvider(LoginProvider.FORM)
                .role(Role.USER)
                .build();
        userService.registerUser(user);

        model.addAttribute("msg", "회원가입 되었습니다.");
        model.addAttribute("url", "/home");
        return "common/alertMsg";
    }

    @GetMapping("/update/{uid}") //
    public String update(@PathParam("uid") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute(user);
        return "users/update";
    }

    // 회원정보 상세보기
    @GetMapping("/{id}/detail")
    public String detail(@PathVariable Long id,
            @RequestParam(name="p", defaultValue = "1") int page,
            Model model){
        Map<String, Object> messagemap = new HashMap<>();

        User user = userService.findById(id);
        model.addAttribute("user", user);
        Page<Post> postList = postService.getPagedPostsByUserId(page, user);
        model.addAttribute("postList", postList);

        return "users/detail";
    }

    // 사용자 정보 수정
    @PostMapping("/{uid}") // 변경 가능 필드(비밀번호, 실명, 닉네임, 프로필사진)
    @ResponseBody
    public Map<String, Object> updateProc(@PathVariable("uid") Long id,
            @Validated UpdateDto updateDto) {
        Map<String, Object> messagemap = new HashMap<>();
        userService.updateUser(id, updateDto);
        messagemap.put("message","회원정보가 수정되었습니다.");
        String redirectUrl = "/"+id+"/detail";
        messagemap.put("redirectUrl",redirectUrl);
        return messagemap;
    }

    // 이메일 중복확인
    @GetMapping("/check-email")
    @ResponseBody //
    public Map<String, Object> checkUserExist(@RequestParam(name="email") String email) {
        Map<String, Object> messagemap = new HashMap<>();
        if (userService.findByEmail(email) != null) {
            throw new ExpectedException(ErrorCode.ID_ALREADY_EXIST);
        }
        messagemap.put("message", "이메일("+email+")을 사용할 수 있습니다.");
        return messagemap;
    }

    // 닉네임 중복확인
    @GetMapping("/check-nickname")
    @ResponseBody //
    public Map<String, Object> checkNicknameExist(@RequestParam(name="nickname") String nickname) {
        Map<String, Object> messagemap = new HashMap<>();
        if (userService.findByNickname(nickname) != null) {
            throw new ExpectedException(ErrorCode.NICKNAME_ALREADY_EXIST);
        }
        messagemap.put("message", "닉네임 " + nickname +"을 사용할 수 있습니다.");
        return messagemap;
    }

    // 회원 탈퇴
    @GetMapping("/{id}")
    public String delete(@PathVariable Long id,  HttpServletResponse response) {
        //탈퇴 시 쿠키 제거해 redirection 방지
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        userService.deleteUser(id); //soft delete
        return "redirect:/api/users/custom-login";
    }


    // 로그인 성공 시
    @GetMapping("/loginSuccess") // json return, home으로 redirect,
    public String loginSuccess(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        System.out.println("---------아이디"+user.getId());
        System.out.println("---------이메일"+user.getEmail());
        session.setAttribute("sessId", user.getId());
        String url = "/home";
        String msg = user.getNickname() + "님 환영합니다.";
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "common/alertMsg";
    }

    // 로그인 성공 시
    @GetMapping("/loginSuccessV2") // json return, home으로 redirect,
    public String loginSuccessV2(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println("authentication.getName(): " + authentication.getName());
        User user = userService.findByEmail(email);
        System.out.println("---------아이디"+user.getId());
        System.out.println("---------이메일"+user.getEmail());
        String url = "/home";
        String msg = user.getNickname() + "님 환영합니다.";
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "common/alertMsg";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/home";
    }

    @PostMapping("/find-password")
    @ResponseBody //
    public Map<String, Object> findPassword(@Validated @RequestBody UserFindPasswordDto findPasswordDto) {
        Map<String, Object> messagemap = new HashMap<>();
        User user = userService.findByEmail(findPasswordDto.getEmail());

        if (user == null) {
            throw new ExpectedException(ErrorCode.USER_NOT_FOUND);
        }

        if (user.getLoginProvider() != LoginProvider.FORM) {
            throw new ExpectedException(ErrorCode.ID_CONFLICT);
        }

        String savedPhoneNumber = user.getPhoneNumber();

        if (!savedPhoneNumber.equals(findPasswordDto.getPhoneNumber())) {
            throw new ExpectedException(ErrorCode.PROFILE_PHONE_MISMATCH);
        }
        messagemap.put("message", "비밀번호를 변경하세요.");
        messagemap.put("redirectUrl", "redirect:/api/users/change-password");
        return messagemap;
    }

    @PostMapping("/change-password")
    @ResponseBody //
    public Map<String, Object> changePassword(@Validated @RequestBody UserChangePasswordDto changePasswordDto) {
        Map<String, Object> messagemap = new HashMap<>();

        messagemap.put("message", "비밀번호가 변경되었습니다.");
        messagemap.put("redirectUrl", "redirect:/api/users/home");
        return messagemap;
    }

    @PostMapping("/find-id")
    @ResponseBody //
    public Map<String, Object> findId(@Validated @RequestBody UserFindIdDto findIdDto) {
        Map<String, Object> messagemap = new HashMap<>();
        List<User> users = userService.findByUsernameAndPhoneNumber(findIdDto.getUname(), findIdDto.getPhoneNumber());

        if (users.isEmpty()) {
            throw new ExpectedException(ErrorCode.USER_NOT_FOUND);
        }

        List<String> emailList = users.stream().map(User::getEmail).toList();

        messagemap.put("message", "정보와 일치하는 아이디 목록입니다.");
        messagemap.put("emailList", emailList);
        return messagemap;
    }
}
