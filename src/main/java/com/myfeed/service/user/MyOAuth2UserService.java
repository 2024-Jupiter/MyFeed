package com.myfeed.service.user;

import com.myfeed.model.user.LoginProvider;
import com.myfeed.model.user.MyUserDetails;
import com.myfeed.model.user.User;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
@Slf4j
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired private UserService userService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String uid, email, uname, profileUrl;
        String hashedPwd = bCryptPasswordEncoder.encode("Social Login");
        User user = null;

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        switch (provider) {
            // public User(String email, String password, String username, String nickname, String profileImage, LoginProvider loginProvider) {
            case "kakao":
                Map<String, Object> account = (Map) oAuth2User.getAttribute("kakao_account");
                email = (String) account.get("email");
                user = userService.findByEmail(email);

                if (user == null) {
                    long kid = (long) oAuth2User.getAttribute("id");
                    Map<String, String> properties = (Map) oAuth2User.getAttribute("properties");
                    account = (Map) oAuth2User.getAttribute("kakao_account");
                    String nickname = (String) properties.get("nickname");
                    nickname = (nickname == null) ? "k_"+kid : nickname;
                    email = (String) account.get("email");
                    profileUrl = (String) properties.get("profile_image");
                    user = new User(email, hashedPwd, nickname, nickname,profileUrl, LoginProvider.KAKAO);
                    userService.registerUser(user);
                }
                break;

        }
        return new MyUserDetails(user, oAuth2User.getAttributes());
    }
}
