package invincibleDevs.bookpago.Users.service;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import invincibleDevs.bookpago.Users.dto.request.KakaoSignInRequest;
import invincibleDevs.bookpago.Users.dto.request.SignInRequest;
import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.common.JWTUtil;
import invincibleDevs.bookpago.common.Utils;
import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.repository.ProfileRepository;
import invincibleDevs.bookpago.Users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserEntityService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final JWTUtil jwtUtil;
    public boolean isUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public SignInResponse signInUser(SignInRequest signInRequest) { //기능 : 유저아이디 널이면 , false반환. 유저아이디 잇으면 아디 이름 반환.
       try {
           String username = Utils.getAuthenticatedUsername();
           System.out.println("testnameeee");
           System.out.println(username);
           UserEntity userEntity = userRepository.findByUsername(username);
           return new SignInResponse(
                   true,
                   Optional.ofNullable(username),
                   Optional.ofNullable(userEntity.getNickname()), // Make sure to pass `nickname` if needed
                   Optional.ofNullable(signInRequest.serverToken())
           );

       }catch (CustomException e) {
           return new SignInResponse(false, Optional.empty(), Optional.empty(),Optional.empty());

       }
//        if (userRepository.existsByUsername(username)) { //DB엔 String타입 저장되어있음
//            UserEntity userEntity = userRepository.findByUsername(signInRequest.username().toString());
//            String serverToken = jwtUtil.createJwt(userEntity.getUsername(), "USER",60*60*1000*10L);
//            return new SignInResponse(true, Optional.ofNullable(userEntity.getUsername()), Optional.ofNullable(userEntity.getNickname()),Optional.ofNullable(serverToken));
//        } else {
//            return new SignInResponse(false, Optional.empty(), Optional.empty(),Optional.empty());
//        }
    }

    @Transactional
    public SignInResponse kakaoSignInUser(KakaoSignInRequest kakaoSignInRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoSignInRequest.kakaoAccessToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me", //post요청보낼 주소
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        System.out.println(response);

        // JSON 문자열 파싱
        JsonObject rootObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

        // id와 nickname 추출
        long id = rootObject.get("id").getAsLong();
        String username = Long.toString(id);

        if (userRepository.existsByUsername(username)) { //DB엔 String타입 저장되어있음
            UserEntity userEntity = userRepository.findByUsername(username);
            String serverToken = jwtUtil.createJwt(userEntity.getUsername(), "USER",60*60*1000*10L);
            return new SignInResponse(true, Optional.ofNullable(userEntity.getUsername()), Optional.ofNullable(userEntity.getNickname()),Optional.ofNullable(serverToken));
        } else {
            return new SignInResponse(false, Optional.empty(), Optional.empty(),Optional.empty());
        }

    }


    @Transactional
    public SignUpResponse kakaoJoinUser(KakaoJoinRequest kakaoJoinRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoJoinRequest.kakaoOauthToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me", //post요청보낼 주소
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        System.out.println(response);

        // JSON 문자열 파싱
        JsonObject rootObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

        // id와 nickname 추출
        long id = rootObject.get("id").getAsLong();
        String username = Long.toString(id);
        String nickname = rootObject.get("properties").getAsJsonObject().get("nickname").getAsString();

        // 응답의 상태 코드 확인
        if (response.getStatusCode().is2xxSuccessful()) {
            UserEntity userEntity = UserEntity.builder()
                    .username(username)
                    .nickname(kakaoJoinRequest.nickname())
                    .gender(kakaoJoinRequest.gender())
                    .age(kakaoJoinRequest.age())
                    .created_at(LocalDateTime.now())
                    .build();
            userRepository.save(userEntity);
            Profile profile = Profile.builder()
                    .userEntity(userEntity)
                    .build();
            profileRepository.save(profile);
            String serverToken = jwtUtil.createJwt(userEntity.getUsername(), "USER",60*60*1000*10L);
            return new SignUpResponse(userEntity.getUsername(), userEntity.getNickname(),serverToken);
        } else{
            throw new IllegalArgumentException("Invalid access token: " + response.getBody());
        }

    }
}
