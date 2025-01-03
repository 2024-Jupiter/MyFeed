
package com.myfeed.controller;

import com.myfeed.exception.CustomException;
import com.myfeed.model.post.Post;
import com.myfeed.model.user.LoginProvider;
import com.myfeed.model.user.RegisterDto;
import com.myfeed.model.user.Role;
import com.myfeed.model.user.UpdateDto;
import com.myfeed.model.user.User;
import com.myfeed.model.user.UserChangePasswordDto;
import com.myfeed.model.user.UserFindIdDto;
import com.myfeed.model.user.UserFindPasswordDto;
import com.myfeed.service.Post.PostService;
import com.myfeed.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/users")
public class UserController {
    @Autowired UserService userService;
    @Autowired PostService postService;

    // 로그인
    @GetMapping("/custom-login")
    @ResponseBody
    public Map<String, Object> loginForm() {
        Map<String, Object> messagemap = new HashMap<>();
        messagemap.put("message", "로그인이 필요합니다.");
        return messagemap;
        //return "users/home"
    }

    // 회원 가입(폼)
    @GetMapping("/register")
    public String registerForm(){
        return "users/register";
    }

    @PostMapping("/register")
    @ResponseBody //
    public Map<String, Object> registerProc(@Validated @RequestBody RegisterDto registerDto){
        Map<String, Object> messagemap = new HashMap<>();
        String hashedPwd = BCrypt.hashpw(registerDto.getPwd(), BCrypt.gensalt());
        User user = User.builder()
                .email(registerDto.getEmail()).password(hashedPwd)
                .username(registerDto.getUname()).nickname(registerDto.getNickname())
                .profileImage(registerDto.getProfileImage())
                .phoneNumber(registerDto.getPhoneNumber())
                .loginProvider(LoginProvider.FORM)
                .role(Role.USER)
                .build();
        userService.registerUser(user);
        messagemap.put("message","회원가입 되었습니다.");
        messagemap.put("redirectUrl","/home");
        return messagemap;
    }

    @GetMapping("/update/{uid}")
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
    @ResponseBody //
    public Map<String, Object> updateProc(@PathVariable("uid") Long id,
                                          @Validated @RequestBody UpdateDto updateDto) {
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
            throw new CustomException("409", "이미 회원가입된 이메일입니다.");
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
            throw new CustomException("409", "이미 존재하는 닉네임입니다.");
        }
        messagemap.put("message", "닉네임 " + nickname +"을 사용할 수 있습니다.");
        return messagemap;
    }

    // 회원 탈퇴
    @GetMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id); //soft delete
        return "redirect:/home";
    }

    // 로그인 성공 시
    @GetMapping("/loginSuccess") // json return, home으로 redirect,
    public String loginSuccess(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        session.setAttribute("sessId", user.getId());
        String msg = user.getNickname() + "님 환영합니다.";
        model.addAttribute("msg", msg);
        return "home";
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
        return "users/list";
    }

    //회원 활성/비활성 여부 수정하기
    @PostMapping("/{uid}/status")
    public String updateUserState(@PathVariable Long id,
                                  @RequestParam(name="status") boolean status,
                                  Model model) {
        userService.updateUserStatus(id, status);
        //todo model로 넘겨주는 parameter 추가 예정,,
        return "redirect:/users/list";
    }

    @PostMapping("/find-password")
    @ResponseBody //
    public Map<String, Object> findPassword(@Validated @RequestBody UserFindPasswordDto findPasswordDto) {
        Map<String, Object> messagemap = new HashMap<>();
        User user = userService.findByEmail(findPasswordDto.getEmail());

        if (user == null) {
            throw new CustomException("404", "아이디가 존재하지 않습니다.");
        }

        String savedPhoneNumber = user.getPhoneNumber();

        if (!savedPhoneNumber.equals(findPasswordDto.getPhoneNumber())) {
            throw new CustomException("403", "휴대폰 번호가 기존 정보와 일치하지 않습니다.");
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
            throw new CustomException("404", "정보와 일치하는 회원이 존재하지 않습니다.");
        }

        List<String> emailList = users.stream().map(User::getEmail).toList();

        messagemap.put("message", "정보와 일치하는 아이디 목록입니다.");
        messagemap.put("emailList", emailList);
        return messagemap;
    }
}
