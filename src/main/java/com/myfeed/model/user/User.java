package com.myfeed.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
