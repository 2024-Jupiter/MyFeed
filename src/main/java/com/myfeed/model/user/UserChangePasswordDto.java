package com.myfeed.model.user;

import com.myfeed.annotation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@PasswordMatch
public class UserChangePasswordDto {

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min=8,message = "8자리 이상의 비밀번호를 입력하세요.")
    private String pwd;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String pwd2;


}
