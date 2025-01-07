package com.myfeed.config;

import com.myfeed.exception.CustomException;
import com.myfeed.exception.ExpectedException;
import com.myfeed.response.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String url = "/api/users/custom-login";
        String errorMsg;


      if (exception instanceof BadCredentialsException) { // UsernameNotFoundException 여기서 잡힘
            errorMsg = "아이디 또는 비밀번호가 틀렸습니다.";
            System.out.println(errorMsg);
            throw new ExpectedException(ErrorCode.BAD_CREDENTIALS);
        } else if (exception instanceof DisabledException) { //O
            errorMsg = "비활성화된 회원입니다. 관리자에게 문의하세요";
            url = "/api/users/home";
            System.out.println(errorMsg);
            throw new ExpectedException(ErrorCode.USER_DISABLED);
        } else {
            errorMsg = "로그인 중 오류가 발생했습니다. 다시 시도해주세요.";
            System.out.println(errorMsg);
            throw new ExpectedException(ErrorCode.LOGIN_ERROR);
        }
    }
}