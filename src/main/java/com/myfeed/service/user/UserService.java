package com.myfeed.service.user;

import com.myfeed.model.user.User;

public interface UserService {
    User findByUid(String uid);

    void updateUser(User user);

    void registerUser(User user);

    void deleteUser(String uid);
}
