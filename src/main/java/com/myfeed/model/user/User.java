package com.myfeed.model.user;

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

    @Column(nullable = false)
    private String email; // 필수값

    @Column(nullable = false)
    private String password; // 필수값

    @Column(nullable = false)
    private String username;

    @Column
    private String phoneNumber; // 폼 로그인 시 필수값

    @Column(nullable = false)
    private String nickname; // 필수값

    @Column
    private String profileImage;

    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private LoginProvider loginProvider;

    @Column(nullable = false, name = "state")
    private boolean isActive = true; // status

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column
    private LocalDateTime deletedAt;
}