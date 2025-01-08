package com.myfeed.service.user;

import com.myfeed.model.user.RegisterDto;
import com.myfeed.model.user.UpdateDto;
import com.myfeed.model.user.User;
import com.myfeed.model.user.UserFindPasswordDto;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface UserService {
    public static final int PAGE_SIZE = 20;

    User findById(Long uid);

    List<User> findByUsernameAndPhoneNumber(String username, String phoneNumber);

    List<User> findByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    User findByNickname(String nickname);

    void updateUser(Long id, UpdateDto updateDto);

    void updateUserStatus(Long id, boolean status);

    void registerUser(RegisterDto registerDto);

    void saveUser(User user);

    void deleteUser(Long uid);

    void setTempPassword(String email, String tempPassword);

    Page<User> getPagedUser(int page, boolean isActive);

    void checkUserInfoMatch(User user, String phoneNumber);

    void deleteUserAccessToken(HttpServletResponse response);
}
