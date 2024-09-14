package invincibleDevs.bookpago.Users.facade;

import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.request.SignInRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import invincibleDevs.bookpago.Users.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserEntityService userEntityService;
    public SignInResponse signInUser(SignInRequest signInRequest) {
        return userEntityService.signInUser(signInRequest);
    }

    public SignUpResponse kakaoJoinUser(KakaoJoinRequest kakaoJoinRequest) {
        try {
            return userEntityService.kakaoJoinUser(kakaoJoinRequest);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid access token : " + e.getMessage());
            // 예외를 다시 던지거나, 사용자에게 적절한 응답을 반환합니다.
            throw e;
        }

    }
}
