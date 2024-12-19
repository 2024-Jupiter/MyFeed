package com.myfeed.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    private Long uid;

    private String password;

    private String uname;

    private String nickname;

    private String email;

    @Column(nullable = true)
    private String profileImage;

    private String role;


    public User(Long uid, String password, String uname, String nickname, String email, String profileImage) {
        this.uid = uid;
        this.password = password;
        this.uname = uname;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.role = "ROLE_USER";
    }
}
