package invincibleDevs.bookpago.common;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 카카오에서 가져온 사용자 정보
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오 사용자 ID를 가져와 username으로 사용
        String username = (String) attributes.get("id"); // 카카오 ID

        // DB에서 사용자 확인


        // OAuth2User 객체 생성
        OAuth2User customOAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id" // 사용자 식별자 키
        );

        // Authentication 객체 생성하여 SecurityContext에 설정
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, customOAuth2User.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return customOAuth2User;
    }
}
