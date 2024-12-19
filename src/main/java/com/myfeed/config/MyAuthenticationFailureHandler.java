package com.myfeed.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String url = "/api/user/login";
        String errorMsg;

        if (exception instanceof BadCredentialsException) {
            errorMsg = "아이디 또는 비밀번호가 틀렸습니다.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMsg = "존재하지 않는 아이디입니다.";
            url = "/api/user/register";
        } else if (exception instanceof DisabledException) {
            errorMsg = "비활성화된 회원입니다.";
            url = "/api/user/register";
        } else {
            errorMsg = "로그인 중 오류가 발생했습니다. 다시 시도해주세요.";
        }
        // 실패 메세지 전달
        request.getSession().setAttribute("error", errorMsg);

        // 리다이렉션
        response.sendRedirect(url);
    }
}