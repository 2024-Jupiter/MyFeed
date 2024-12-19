package com.myfeed.service.user;

import com.myfeed.model.user.User;
import com.myfeed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User findByUid(String uid) {
        return userRepository.findById(uid).orElse(null);
    }

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }


    @Override
    public void deleteUser(String uid) {
        userRepository.deleteById(uid);
    }

}