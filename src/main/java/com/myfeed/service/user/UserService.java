package com.myfeed.service.user;

import com.myfeed.model.user.User;

public interface UserService {

    User findById(Long uid);

    User findByUsername(String username);

    User findByEmail(String email);

    void updateUser(User user);

    void registerUser(User user);

    void deleteUser(Long uid);
}
