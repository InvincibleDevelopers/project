package invincibleDevs.bookpago.Users;

import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.request.KakaoSignInRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UserFacade userFacade;

    @GetMapping("/login")
    public ResponseEntity<SignInResponse> signIn(
            @ApiParam(value = "serverToken", required = true)
            @RequestParam(value = "kakaoId") Long kakaoId
    ) {
        return ResponseEntity.ok(userFacade.signInUser(kakaoId));
    }


    @GetMapping("/kakaologin")
    public ResponseEntity<SignInResponse> kakaoLogin(
            @ApiParam(value = "kakaoAccessToken", required = true)
            @RequestHeader(value = "Authorization", required = true) String kakaoAccessToken
    ) {
        KakaoSignInRequest kakaoSignInRequest = new KakaoSignInRequest(kakaoAccessToken);
        return ResponseEntity.ok(userFacade.kakaoLoginUser(kakaoSignInRequest));
    }

    @PostMapping("/kakaojoin")
    public ResponseEntity<SignUpResponse> kakaoJoin(  //프론트가 액세스토큰 body에 담아서 줌
            @ApiParam(value = "회원 등록 정보, 카카오 토큰 필수")
            @RequestBody KakaoJoinRequest kakaoJoinRequest
    ) {
        return ResponseEntity.ok(userFacade.kakaoJoinUser(kakaoJoinRequest));
    }

    @GetMapping("/check")
    public String checkUser(

            @RequestHeader(value = "Authorization", required = true) String serverToken
    ) {
//        SignInRequest signInRequest = new SignInRequest(serverToken);
//        System.out.println(username);
        System.out.println(serverToken);
//        if (userRepository.existsByUsername(username)) {
//            UserEntity userEntity  = userRepository.findByUsername(username);
//            return userEntity.getNickname();
//        }

        return "error";
    }


}
