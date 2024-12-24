package com.myfeed.repository;

import com.myfeed.model.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Page<User> findAllByActiveTrue(Pageable pageable);
    Page<User> findAllByActiveFalse(Pageable pageable);
}