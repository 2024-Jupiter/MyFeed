package com.myfeed.service.user;

import com.myfeed.model.user.MyUserDetails;
import com.myfeed.model.user.User;
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
            case "kakao":
                long kid = (long) oAuth2User.getAttribute("id"); //todo
                uid = provider + "_" + kid;
                user = userService.findByEmail(uid);
                if (user != null && !user.isActive()) {
                    throw new DisabledException("비활성화된 회원입니다.");
                }

                if (user == null) {
                    Map<String, String> properties = (Map) oAuth2User.getAttribute("properties");
                    Map<String, Object> account = (Map) oAuth2User.getAttribute("kakao_account");
                    uname = (String) properties.get("nickname");
                    uname = (uname == null) ? "kakao_user" : uname;
                    email = (String) account.get("email");
                    profileUrl = (String) properties.get("profile_image");
                    user = new User(email, hashedPwd, uname, uname,profileUrl);
                    userService.registerUser(user);
                }
                break;
        }
        return new MyUserDetails(user, oAuth2User.getAttributes());
    }
}
