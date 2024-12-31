package com.myfeed.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.myfeed.jwt.JwtRequestFilter;
import com.myfeed.jwt.JwtTokenUtil;
import com.myfeed.model.user.User;
import com.myfeed.service.user.UserService;
import com.myfeed.sms.SmsController;
import com.myfeed.sms.SmsRequestDto;
import com.myfeed.sms.SmsResponseDto;
import com.myfeed.sms.SmsService;
import com.nimbusds.jose.shaded.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

//@WebMvcTest(SmsController.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SmsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private DefaultMessageService messageService;

    // @MockBean
    @Autowired
    private SmsService smsService;

    @MockBean
    private UserService userService;

    private final Gson gson = new Gson();

    @DisplayName("메세지 전송 성공")
    @Test
    void sendMessage_Success() throws Exception {
        //given
        User user = User.builder()
                .email("sarah2316@naver.com").password("password")
                .username("혜란")
                .nickname("gPfks")
                .phoneNumber("1234")
                .build();
        List<User> users = new ArrayList<>();
        users.add(user);

        SmsRequestDto smsRequestDto = new SmsRequestDto();
        smsRequestDto.setPhoneNumber("01077052827");
        String authCode = "123123";
        SmsResponseDto smsResponseDto = new SmsResponseDto();
        smsResponseDto.setAuthCode(authCode);
        SingleMessageSentResponse mockResponse = new SingleMessageSentResponse(
                "group123",
                "01077052827",
                "01077052827",
                MessageType.SMS,
                "Success",
                "KR",
                "message123",
                "200",
                "account123"
        );

        Mockito.when(userService.findByPhoneNumber(smsRequestDto.getPhoneNumber())).thenReturn(users);
        //Mockito.when(smsService.sendMessage(any(SmsRequestDto.class))).thenReturn("123123");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/send-sms/send-authcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(smsRequestDto)))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @DisplayName("메세지 전송 실패 - 해당 번호 사용자 없음")
    @Test
    void sendMessage_Failure() throws Exception {
        //given
        List<User> users = new ArrayList<>();


        SmsRequestDto smsRequestDto = new SmsRequestDto();
        smsRequestDto.setPhoneNumber("01012345678");

        SingleMessageSentResponse mockResponse = new SingleMessageSentResponse(
                "group123",
                "01012345678",
                "01077052827",
                MessageType.SMS,
                "Success",
                "KR",
                "message123",
                "200",
                "account123"
        );

        Mockito.when(userService.findByPhoneNumber(smsRequestDto.getPhoneNumber())).thenReturn(users);
        //Mockito.when(smsService.sendMessage(any(SmsRequestDto.class))).thenReturn("123123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/send-sms/send-authcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(smsRequestDto)))
                .andExpect(status().isNotFound())
                .andDo(print());

    }


}