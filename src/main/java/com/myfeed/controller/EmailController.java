package com.myfeed.controller;

import com.myfeed.model.email.EmailMessage;
import com.myfeed.model.email.EmailPostDto;
import com.myfeed.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/send-email")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/password")
    public ResponseEntity sendPasswordMail(@RequestParam EmailPostDto emailPostDto) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailPostDto.getEmailAddress())
                .subject("[MyFeed] 비밀번호 찾기")
                .build();

        emailService.sendMail(emailMessage, "password");
        // 성공 시 HTTP 200 OK 상태코드 반환
        return ResponseEntity.ok().build();
    }




}
