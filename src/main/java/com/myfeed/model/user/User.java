package com.myfeed.model.user;

import com.myfeed.model.post.BlockStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // 필수값

    private String password; // 필수값

    private String username;

    @Column(nullable = true)
    private String phoneNumber; // 폼 로그인 시 필수값

    private String nickname; // 필수값

    @Column(nullable = true)
    private String profileImage;

    private Role role;

    private LoginProvider loginProvider;

    private boolean isActive;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
