package com.myfeed.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min=8,message = "8자리 이상의 비밀번호를 입력하세요.")
    private String pwd;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String pwd2;

    @NotBlank(message = "이름을 입력하세요.")
    private String uname;

    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;

    @NotBlank(message = "전화번호를 입력하세요.")
    @Size(min = 4, max = 4, message = "전화번호 4자리를 입력하세요")
    private String phoneNumber;

    private String profileImage;


}