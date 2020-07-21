package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.config.auth.dto.OAuthAttributes;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userReposiotry;
    private final HttpSession httpsession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        // ex) 구글 로그인인지, 네이버 로그인인지
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값 = primary key
        // 구글은 기본적으로 지원, 네이버 카카오 등은 기본 지원 x
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // Outh2UserService를 통해 가져온 OAuth2USer의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        // SessionUser = 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        // 왜 User 클래스를 사용하지 않고 새로 만들어 쓰는 지는 뒤에 설명
        httpsession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userReposiotry.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userReposiotry.save(user);
    }
}
