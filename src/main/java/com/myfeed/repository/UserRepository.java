package com.myfeed.repository;

import java.util.Optional;

import com.myfeed.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    Page<User> findAllByActiveTrue(Pageable pageable);
    Page<User> findAllByActiveFalse(Pageable pageable);
}