package com.myfeed.service.user;

import com.myfeed.model.user.User;
import com.myfeed.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String uname) { // todo
        return userRepository.findByUsername(uname).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
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
    public void setTempPassword(String email, String tempPassword) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setPassword(tempPassword);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long uid) {
        userRepository.deleteById(uid);
    }

    @Override
    public Page<User> getPagedUser(int page, boolean isActive) {
        Pageable pageable = PageRequest.of(page-1,PAGE_SIZE);
        Page<User> userPage = null;
        if (isActive) {
            userPage = userRepository.findAllByActiveTrue(pageable);
        } else {
            userPage = userRepository.findAllByActiveFalse(pageable);
        }
        return null;
    }
}
