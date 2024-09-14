package invincibleDevs.bookpago.Users;

import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.request.SignInRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import invincibleDevs.bookpago.Users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersFacade {
    private final UsersService usersService;
    public SignInResponse signInUser(SignInRequest signInRequest) {
        return usersService.signInUser(signInRequest);
    }

    public SignUpResponse kakaoJoinUser(KakaoJoinRequest kakaoJoinRequest) {
        try {
            return usersService.kakaoJoinUser(kakaoJoinRequest);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid access token : " + e.getMessage());
            // 예외를 다시 던지거나, 사용자에게 적절한 응답을 반환합니다.
            throw e;
        }

    }
}
