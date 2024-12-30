package com.myfeed.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.myfeed.jwt.JwtRequestFilter;
import com.myfeed.jwt.JwtTokenUtil;
import com.myfeed.model.user.User;
import com.myfeed.sms.SmsController;
import com.myfeed.sms.SmsDto;
import com.nimbusds.jose.shaded.gson.Gson;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import net.nurigo.sdk.message.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(SmsController.class)
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

    private final Gson gson = new Gson();

//    @DisplayName("이메일 중복확인 성공 - 신규 이메일")
//    @Test
//    void emailNotExist() throws Exception {
//        // given
//        User user = User.builder()
//                .email("sarah2316@naver.com").password("password")
//                .username("혜란")
//                .nickname("gPfks")
//                .phoneNumber("1234")
//                .build();
//        Mockito.when(userService.findByEmail("sarah2316@naver.com")).thenReturn(user);
//
//
//        // when
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/check-email")
//                        .param("email","sarah1217@naver.com").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//

    @DisplayName("메세지 전송 성공")
    @Test
    void sendMessage_Success() throws Exception {
        //given
        SmsDto smsDto = SmsDto.builder().phoneNumber("01077052827").build();

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

        Mockito.when(messageService.sendOne(any(SingleMessageSendingRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/send-sms/send-one")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(smsDto)))
                .andExpect(jsonPath("$.to").value("01077052827"))
                .andDo(print());

    }


}