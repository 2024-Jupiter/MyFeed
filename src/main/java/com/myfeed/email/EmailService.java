package com.myfeed.email;

import com.myfeed.email.model.EmailMessage;
import com.myfeed.exception.UserNotFoundException;
import com.myfeed.service.user.UserService;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final SpringTemplateEngine templateEngine;
    private final UserService userService;

    public String sendMail(EmailMessage emailMessage, String type) {
        if (userService.findByEmail(emailMessage.getTo()) == null) {
            throw new UserNotFoundException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        String authNum = createCode();


        if (type.equals("password")) {
            userService.setTempPassword(emailMessage.getTo(), authNum);
        }

        try {
            log.info("Email sent to " + emailMessage.getTo());
            return authNum;
        } catch (Exception e) {
            log.error("Failed to send email to " + emailMessage.getTo());
            throw new RuntimeException(e);
        }
    }

    //임시 비밀번호 생성
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i<8; i++) {
            int idx = random.nextInt(4);
            switch (idx) {
                case 0: key.append((char) (random.nextInt(26) + 97)); break;
                case 1: key.append((char) (random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    // 안내 html 만들기
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        // type에 따라 다른 경로 html 파일을 불러오기(src/main/resources/templates/.. .html)
        return templateEngine.process(type, context);
    }

}
