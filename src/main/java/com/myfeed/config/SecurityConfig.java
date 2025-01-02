package com.myfeed.config;


import com.myfeed.jwt.JwtRequestFilter;
import com.myfeed.model.user.Role;
import com.myfeed.service.user.MyOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired private AuthenticationFailureHandler failureHandler;
    @Autowired private MyOAuth2UserService myOAuth2UserService;
    @Autowired private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable())       // CSRF 방어 기능 비활성화
                .headers(x -> x.frameOptions(y -> y.disable()))     // H2-console
                .authorizeHttpRequests(requests -> requests
                        //register @validated 예외 발생 시 loginPage("/api/users/custom-login")로 넘어가는(권한 요청하는) 문제
                        .requestMatchers("/api/users/find-id" ,"/api/users/find-password" ,"/api/users/check-email","/api/users/check-nickname", "/api/users/custom-login","/api/users/register", "/api/replies/**", "/api/posts/**", "/api/postEs/**", "/api/users/*/detail", "/api/users/**", "/view/home", "/api/admin/reports/posts/{postId}", "/api/admin/reports/replies/{replyId}").permitAll()
                        .requestMatchers("/api/admin/users/*/status", "/api/admin/users", "/api/admin/reports/**", "/posts/**?category=NEWS").hasAuthority(String.valueOf(Role.ADMIN))
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                        .loginPage("/api/users/custom-login") // template return url users/loginPage
                        .loginProcessingUrl("/api/users/login")  // post 엔드포인트
                        .usernameParameter("email")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/api/users/loginSuccess", false)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(auth -> auth
                        .logoutUrl("/users/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/users/login")
                )
                .oauth2Login(auth -> auth
                        .loginPage("/users/login")
                        .userInfoEndpoint(user -> user.userService(myOAuth2UserService))
                        .defaultSuccessUrl("/users/loginSuccess", true)
                        .failureHandler(failureHandler)
                )
        ;
        // JwtRequestFilter 추가
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }
}
