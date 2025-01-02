package com.myfeed.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String nickname;

    public UserDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
    }
}
