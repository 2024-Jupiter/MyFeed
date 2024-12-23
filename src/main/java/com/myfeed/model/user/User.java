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

    private String email;

    private String password;

    private String username;

    private String nickname;

    @Column(nullable = true)
    private String profileImage;

    private String role;

    private boolean isActive;

    // 블락 처리
    @Enumerated(EnumType.STRING)
    private BlockStatus blockStatus = BlockStatus.NORMAL_STATUS;

    private LocalDateTime blockAt;

    private LocalDateTime unBlockAt;

    public User(String email, String password, String username, String nickname, String profileImage) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = "ROLE_USER";
        this.isActive = true;
    }
}
