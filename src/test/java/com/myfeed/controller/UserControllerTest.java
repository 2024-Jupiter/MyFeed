package com.myfeed.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.myfeed.MyfeedApplication;
import com.myfeed.model.user.RegisterDto;
import com.myfeed.service.user.UserService;
import com.nimbusds.jose.shaded.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class) //
@ContextConfiguration(classes = MyfeedApplication.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Gson gson = new Gson();

    @MockBean
    private UserService userService;

    @DisplayName("사용자 추가 실패 - 이메일 형식이 아님")
    @Test
    void addUserFail_NotEmailFormat() throws Exception {
        // given
        final RegisterDto registerDto = RegisterDto.builder()
                .email("mangkyu")
                .pwd("password")
                .pwd2("password")
                .uname("혜란")
                .nickname("gPfks")
                .phoneNumber("1234")
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user/register")
                        .content(gson.toJson(registerDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());

    }

    @DisplayName("사용자 추가 성공")
    @Test
    void addUserFail() throws Exception {
        // given
        final RegisterDto registerDto = RegisterDto.builder()
                .email("mangkyu@naver.com")
                .pwd("password")
                .pwd2("password")
                .uname("혜란")
                .nickname("gPfks")
                .phoneNumber("1234")
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user/register")
                        .content(gson.toJson(registerDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }


}
