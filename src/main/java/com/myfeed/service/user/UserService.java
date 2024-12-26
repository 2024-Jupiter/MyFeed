package com.myfeed.service.user;

import com.myfeed.model.user.User;
import java.util.List;
import org.springframework.data.domain.Page;

public interface UserService {
    public static final int PAGE_SIZE = 20;

    User findById(Long uid);

    User findByUsername(String username);

    User findByEmail(String email);

    void updateUser(User user);

    void registerUser(User user);

    void deleteUser(Long uid);

    void setTempPassword(String email, String tempPassword);

    Page<User> getPagedUser(int page, boolean isActive);
}
