package invincibleDevs.bookpago.Users;

import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.request.KakaoSignInRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserEntityService userEntityService;

    public SignInResponse signInUser(Long kakaoId) {
        return userEntityService.signInUser(kakaoId);
    }

    public SignInResponse kakaoLoginUser(KakaoSignInRequest kakaoSignInRequest) {
        try {
            return userEntityService.kakaoSignInUser(kakaoSignInRequest);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid access token : " + e.getMessage());
            // 예외를 다시 던지거나, 사용자에게 적절한 응답을 반환합니다.
            throw e;
        }
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
