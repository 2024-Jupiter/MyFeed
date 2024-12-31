package com.myfeed.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFindIdDto {

    @NotBlank(message = "이름을 입력하세요.")
    private String uname;

    @NotBlank(message = "전화번호를 입력하세요.")
    @Size(min = 11, max = 11, message = "전화번호 11자리를 입력하세요")
    private String phoneNumber;
}
