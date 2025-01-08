package com.myfeed.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateDto {

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min=8,message = "8자리 이상의 비밀번호를 입력하세요.")
    private String pwd;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String pwd2;

    @NotBlank(message = "이름을 입력하세요.")
    private String uname;

    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;

    private String profileImage;


}
