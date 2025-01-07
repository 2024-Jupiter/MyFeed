package com.myfeed.jwt;

import com.myfeed.service.user.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired private JwtTokenUtil jwtTokenUtil;
    @Autowired private MyUserDetailsService myUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/users/custom-login") || path.equals("/api/users/register")
                || path.startsWith("/api/send-sms/") || path.equals("/api/users/check-email") || path.equals("/api/users/check-nickname")
                || path.startsWith("/js/") || path.startsWith("/lib/") || path.startsWith("/img/") || path.startsWith("/css/") || path.equals("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("-----------  JwtRequestFilter invoked for: " + request.getRequestURI());

        final String cookieHeader = request.getHeader("Cookie");
        System.out.println("-----------  쿠키: "+cookieHeader);
        //--------------쿠키: accessToken=eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJzYXJhaDIzMTZAbmF2ZXIuY29tIiwic3ViIjoic2FyYWgyMzE2QG5hdmVyLmNvbSIsImlhdCI6MTczNjE1ODk4MiwiZXhwIjoxNzM2MTk0OTgyfQ.z2W7zRlWwu5hN9c5hyEPj1j7ZlVMqFABK5sSXXw7pFw;

        String userId = null;
        String jwt = null;

        if (cookieHeader != null && cookieHeader.startsWith("accessToken=")) {
            jwt = cookieHeader.substring(12);
            userId = (String) jwtTokenUtil.extractUserId(jwt);
            System.out.println("사용자 정보: " + userId);
        }
        // contextHolder에 정보 저장하기
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String userEmail = (String) jwtTokenUtil.extractUserEmail(jwt);
            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(userEmail);

            if (jwtTokenUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);

    }

}
