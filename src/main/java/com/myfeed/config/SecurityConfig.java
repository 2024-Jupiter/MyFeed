package com.myfeed.config;


import com.myfeed.service.user.MyOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig {
    @Autowired private AuthenticationFailureHandler failureHandler;
    @Autowired private MyOAuth2UserService myOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable())       // CSRF 방어 기능 비활성화
                .headers(x -> x.frameOptions(y -> y.disable()))     // H2-console
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/user/register", "/api/board/**","/api/user/**", "/view/home" ).permitAll()
                        .requestMatchers("/api/admin/users/*/status", "/api/admin/users", "/api/admin/boards/report", "/api/admin/boards/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                         .loginPage("/user/login") // template 위치
                        .loginProcessingUrl("/api/user/login")  // 엔드포인트
                        .usernameParameter("email")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/api/user/loginSuccess", true)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(auth -> auth
                        .logoutUrl("/user/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/user/login")
                )
                .oauth2Login(auth -> auth
                        .loginPage("/user/login")
                        .userInfoEndpoint(user -> user.userService(myOAuth2UserService))
                        .defaultSuccessUrl("/user/loginSuccess", true)
                        .failureHandler(failureHandler)
                )
        ;

        return http.build();
    }


}
