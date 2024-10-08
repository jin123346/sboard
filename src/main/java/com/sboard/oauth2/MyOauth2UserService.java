package com.sboard.oauth2;


import com.sboard.entity.User;
import com.sboard.repository.UserRepository;
import com.sboard.security.MyUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
public class MyOauth2UserService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("loadUser..1 : "+userRequest);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        log.info("loadUser..2 accessToken : "+accessToken);

        String provider= userRequest.getClientRegistration().getRegistrationId();
        log.info("loadUser..3 provider : "+provider);


        String uid;
        String name = "";
        String email="";
        String nick;
        Map<String, Object> attributes;

        if ("google".equals(provider)) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("loadUser..4 oAuth2User : "+oAuth2User);


        attributes  = oAuth2User.getAttributes();
        log.info("loadUser..5 attributes : "+attributes);

        // 제공자별 사용자 정보 처리

            email = (String) attributes.get("email");
            uid = email.split("@")[0];
            name = (String) attributes.get("given_name");
            nick = (String) attributes.get("name");
        } else if ("kakao".equals(provider)) {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            log.info("loadUser..4 oAuth2User : "+oAuth2User);


            attributes  = oAuth2User.getAttributes();
            log.info("loadUser..5 attributes : "+attributes);
            Long id = (Long) attributes.get("id");
            // 카카오 사용자 정보에서 properties 가져오기
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            log.info("loadUser..6 properties : "+properties);

            Map<String, Object> kakao_account= (Map<String, Object>) attributes.get("kakao_account");
            log.info("loadUser..7 kakao_account : "+kakao_account);

            Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
            log.info("loadUser..8 profile : "+profile);

            email = (String) kakao_account.get("email");
            log.info("loadUser..9 email : "+email);
            uid= email.split("@")[0];
            name = (String) properties.get("nickname");
            nick = name+id;
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        Optional<User> opt= userRepository.findById(uid);

        if(opt.isPresent()){

            //회원이 존재하면
            User user = opt.get();
            log.info("loadUser..7 user : "+user);

            return MyUserDetails.builder()
                    .user(user)
                    .accessToken(accessToken)
                    .attributes(attributes)
                    .build();
        }else{
            //회원이 존재하지 않으면 회원가입처리


            User user = User.builder()
                    .uid(uid)
                    .name(name)
                    .role("USER")
                    .email(email)
                    .nick(nick)
                    .build();

            userRepository.save(user);

            return MyUserDetails.builder()
                    .user(user)
                    .accessToken(accessToken)
                    .attributes(attributes)
                    .build();
        }


    }
}
