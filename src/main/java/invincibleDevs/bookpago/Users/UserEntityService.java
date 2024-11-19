package invincibleDevs.bookpago.Users;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.request.KakaoSignInRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import invincibleDevs.bookpago.common.JWTUtil;
import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.ProfileRepository;
import invincibleDevs.bookpago.profile.Profile;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final JWTUtil jwtUtil;

    public boolean isUserExists(Long kakaoId) {
        return userRepository.existsByKakaoId(kakaoId);
    }

    public UserEntity getUser(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }

    @Transactional
    public SignInResponse signInUser(
            Long kakaoId) { //기능 : 유저아이디 널이면 , false반환. 유저아이디 잇으면 아디 이름 반환.
        try {
            UserEntity userEntity = userRepository.findByKakaoId(kakaoId);

            // userEntity가 null이면 커스텀 응답 생성
            if (userEntity == null) {
                return new SignInResponse(
                        false, // 로그인 실패
                        Optional.empty(), // 카카오 ID 없음
                        Optional.empty(), // 닉네임 없음
                        null // 프로필 이미지 없음
                );
            }
            return new SignInResponse(
                    true,
                    Optional.ofNullable(userEntity.getKakaoId()),
                    Optional.ofNullable(userEntity.getNickname()),
//                    userEntity.getProfile().getProfileImgUrl()
                    "dddd"
            );

        } catch (CustomException e) {
            return new SignInResponse(false, Optional.empty(), Optional.empty(),
                    null);

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
    public SignInResponse kakaoSignInUser(
            KakaoSignInRequest kakaoSignInRequest) { //자동로그인 튕겼을때, 서버토큰 재발급
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("카카오로그인요청jwt필터해제적용완료");
        HttpHeaders headers = new HttpHeaders();
        System.out.println(kakaoSignInRequest.kakaoAccessToken());
        headers.add("Authorization", "Bearer " + kakaoSignInRequest.kakaoAccessToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

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

        if (userRepository.existsByKakaoId(id)) { //DB엔 String타입 저장되어있음
            UserEntity userEntity = userRepository.findByKakaoId(id);
//            String serverToken = jwtUtil.createJwt(userEntity.getUsername(), "USER",
//                    60 * 60 * 1000 * 10L);
            return new SignInResponse(true,
                    Optional.ofNullable(userEntity.getKakaoId()),
                    Optional.ofNullable(userEntity.getNickname()),
//                    userEntity.getProfile().getProfileImgUrl());
                    "ddd");

        } else {
            return new SignInResponse(false, Optional.empty(), Optional.empty(), null);
        }

    }


    @Transactional
    public SignUpResponse kakaoJoinUser(KakaoJoinRequest kakaoJoinRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoJoinRequest.kakaoOauthToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me", //post요청보낼 주소
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        // JSON 문자열 파싱
        JsonObject rootObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

        // id와 nickname 추출
        long id = rootObject.get("id").getAsLong();
//        String username = Long.toString(id);
        String nickname = rootObject.get("properties").getAsJsonObject().get("nickname")
                                    .getAsString();

        // 응답의 상태 코드 확인
        if (response.getStatusCode().is2xxSuccessful()) {
            UserEntity userEntity = UserEntity.builder()
                                              .kakaoId(id)
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
//            String serverToken = jwtUtil.createJwt(userEntity.getUsername(), "USER", 60 * 1000L);
            return new SignUpResponse(userEntity.getKakaoId(), userEntity.getNickname());
        } else {
            throw new IllegalArgumentException("Invalid access token: " + response.getBody());
        }

    }

    public UserEntity findByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }
}
