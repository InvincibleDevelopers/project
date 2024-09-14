package invincibleDevs.bookpago.Users.controller;

import invincibleDevs.bookpago.Users.UsersFacade;
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
public class UsersController {
    private final UsersFacade usersFacade;
    @GetMapping("/user-check")
    public ResponseEntity<SignInResponse> signInUser(
            @ApiParam(value = "고유 id", required = false)
            @RequestHeader(value = "Authorization", required = false) String Authorization,
            @RequestBody SignInRequest signInRequest){

        return ResponseEntity.ok(usersFacade.signInUser(signInRequest));

//        boolean is = userService.isUserExists(username);
//        if (is) {
//            UserEntity userEntity = new UserEntity();
//            userEntity = userService.getUser(username);
//
//            ProfileDTO profileDTO = new ProfileDTO();
//            profileDTO.setNickname(userEntity.getNickname());
//
//            // JSON 형태로 변환할 Map 객체 생성
//            Map<String, String> response = new HashMap<>();
//            response.put("nickname", profileDTO.getNickname());
//            response.put("username", profileDTO.getUsername());
//
//            return response;
//        }
//        return null ;

    }
//    @PostMapping("/oauth/join")
//    public ResponseEntity<String> test(@RequestBody KakaoUserDTO kakaoUserDTO, @RequestHeader("Authorization") String authorizationHeader){ // 헤더에서 Authorization 값을 받음){
//
//        //accesstoken 오면 한번더 로직거쳐서 암호화해서 토큰보내기
//        String auth = authorizationHeader;
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(kakaoUserDTO.getId());
//        userEntity.setNickname(kakaoUserDTO.getNickname());
//        userEntity.setPassword("00000");
//        userService.setUser(userEntity);
//
//        return ResponseEntity.ok("ok");
//    }
//
//    @PostMapping("/join")
//    public ResponseEntity<String> joinProcess(@RequestBody SignUpRequest signUpRequest) {
//        try {
//            // 회원 가입 처리
//            joinService.joinProcess(signUpRequest);
//
//            // 성공적으로 처리된 경우 200 OK와 메시지 반환
//            return ResponseEntity.ok("회원 가입이 성공적으로 처리되었습니다.");
//        } catch (Exception e) {
//            // 예외가 발생한 경우 500 Internal Server Error와 메시지 반환
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("회원 가입 처리 중 오류가 발생했습니다.");
//        }
//    }
//
//
//
//    @PostMapping("/check")
//    public void checkData(@RequestBody Dto dto,@RequestHeader("Authorization") String authorizationHeader) {
//        System.out.println(authorizationHeader);
//        System.out.println(dto.getBody());
//    }

    @PostMapping("/kakao-join")
    public ResponseEntity<SignUpResponse> kakaoJoin( //프론트가 액세스토큰 bodyㅔ에 담아서 줌
            @ApiParam(value = "회원 등록 정보, 카카오 토큰 필수")
            @RequestHeader("token") String token
            ) {
        KakaoJoinRequest kakaoJoinRequest = new KakaoJoinRequest("testkakao","nickname","servertoken",token,"female",100,"thriller","hihi");
        System.out.println(token);
        return ResponseEntity.ok(usersFacade.kakaoJoinUser(kakaoJoinRequest));
    }



}
