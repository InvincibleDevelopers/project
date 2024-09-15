package invincibleDevs.bookpago.Users.controller;

import invincibleDevs.bookpago.Users.dto.request.KakaoSignInRequest;
import invincibleDevs.bookpago.Users.facade.UserFacade;
import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.request.SignInRequest;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {
    private final UserFacade userFacade;

    @GetMapping("/kakao-login")
    public ResponseEntity<SignInResponse> kakaoLogin(
            @ApiParam(value = "kakaoAccessToken", required = true)
            @RequestHeader(value = "Authorization", required = true) String kakaoAccessToken
    ) {
        KakaoSignInRequest kakaoSignInRequest = new KakaoSignInRequest(kakaoAccessToken);
        return ResponseEntity.ok(userFacade.kakaoLoginUser(kakaoSignInRequest));
    }

//    @GetMapping("/check") //kakao-login으로바꾸기
//    public ResponseEntity<SignInResponse> kakaoLogin(
//            @ApiParam(value = "고유 id", required = false)
//            @RequestHeader(value = "Authorization", required = false) String kakaoAccessToken
//    ) {
//        KakaoSignInRequest kakaoSignInRequest = new KakaoSignInRequest(kakaoAccessToken);
//        return ResponseEntity.ok(userFacade.kakaoLoginUser(kakaoSignInRequest));
//    }

    @PostMapping("/kakao-join")
    public ResponseEntity<SignUpResponse> kakaoJoin(  //프론트가 액세스토큰 body에 담아서 줌
            @ApiParam(value = "회원 등록 정보, 카카오 토큰 필수")
            @RequestBody KakaoJoinRequest kakaoJoinRequest
            ) {
        return ResponseEntity.ok(userFacade.kakaoJoinUser(kakaoJoinRequest));
    }



}
