package invincibleDevs.bookpago.Users.controller;

import invincibleDevs.bookpago.Users.dto.request.KakaoSignInRequest;
import invincibleDevs.bookpago.Users.facade.UserFacade;
import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.request.SignInRequest;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.repository.UserRepository;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {
    private final UserFacade userFacade;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public ResponseEntity<SignInResponse> signIn(
            @ApiParam(value = "serverToken", required = true)
            @RequestParam(value="username") String username
//            @RequestHeader(value = "Authorization", required = true) String serverToken
    ) {
//        SignInRequest signInRequest = new SignInRequest(serverToken);
        return ResponseEntity.ok(userFacade.signInUser(username));
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
//        System.out.println(kakaoJoinRequest.kakaoOauthToken());
        System.out.println(kakaoJoinRequest.username());
        System.out.println(kakaoJoinRequest.age());
        System.out.println("========================================");
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
