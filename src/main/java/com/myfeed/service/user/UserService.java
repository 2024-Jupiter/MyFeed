package com.myfeed.service.user;

import com.myfeed.model.user.UpdateDto;
import com.myfeed.model.user.User;
import org.springframework.data.domain.Page;

public interface UserService {
    public static final int PAGE_SIZE = 20;

    User findById(Long uid);

    User findByUsername(String username);

    User findByEmail(String email);

    User findByNickname(String nickname);

    void updateUser(Long id, UpdateDto updateDto);

    void updateUserStatus(Long id, boolean status);

    void registerUser(User user);

    void deleteUser(Long uid);

    void setTempPassword(String email, String tempPassword);

    Page<User> getPagedUser(int page, boolean isActive);
}
