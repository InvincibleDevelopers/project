package invincibleDevs.bookpago.Users.controller;

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


    @GetMapping("/check")
    public ResponseEntity<SignInResponse> signInUser(
            @ApiParam(value = "고유 id", required = false)
            @RequestHeader(value = "Authorization", required = false) String Authorization,
            @RequestBody SignInRequest signInRequest) {

        return ResponseEntity.ok(userFacade.signInUser(signInRequest));
    }

    @PostMapping("/kakao-join")
    public ResponseEntity<SignUpResponse> kakaoJoin(  //프론트가 액세스토큰 body에 담아서 줌
            @ApiParam(value = "회원 등록 정보, 카카오 토큰 필수")
            @RequestBody KakaoJoinRequest kakaoJoinRequest
            ) {
        return ResponseEntity.ok(userFacade.kakaoJoinUser(kakaoJoinRequest));
    }



}
