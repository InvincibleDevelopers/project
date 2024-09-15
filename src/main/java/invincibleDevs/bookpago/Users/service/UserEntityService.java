package invincibleDevs.bookpago.Users.service;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import invincibleDevs.bookpago.Users.dto.request.SignInRequest;
import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.profile.Profile;
import invincibleDevs.bookpago.profile.ProfileRepository;
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
    public boolean isUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username);
    }

//    public UserEntity setUser(UserEntity userEntity){
//        if (!isUserExists(userEntity.getUsername())){
//            userRepository.save(userEntity);
//            Profile profile = new Profile();
//            profile.setUserEntity(userEntity);
//            profile.setNickName(userEntity.getNickname());
//            profile.setBio("put your text.");
//            profileRepository.save(profile);
//
//        } else{
//            return userEntity;
//        }
//        return userEntity;
//    }
//false 반환받으면 signup 메서드 태우는거.
    @Transactional
    public SignInResponse signInUser(SignInRequest signInRequest) { //기능 : 유저아이디 널이면 , false반환. 유저아이디 잇으면 아디 이름 반환.
        if (userRepository.existsByUsername(signInRequest.userId().toString())){ //DB엔 String타입 저장되어있음
            UserEntity userEntity = userRepository.findByUsername(signInRequest.userId().toString());
            return new SignInResponse(true, Optional.ofNullable(userEntity.getUsername()), Optional.ofNullable(userEntity.getNickname()));
        } else {
            return new SignInResponse(false, Optional.empty(), Optional.empty());
        }
    }

    @Transactional
    public SignUpResponse signUpUser(KakaoJoinRequest kakaoJoinRequest) {
        UserEntity userEntity = UserEntity.builder()
                .username(kakaoJoinRequest.username())
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
        return new SignUpResponse(userEntity.getUsername(), userEntity.getNickname());
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
            return new SignUpResponse(userEntity.getUsername(), userEntity.getNickname());
        } else{
            throw new IllegalArgumentException("Invalid access token: " + response.getBody());
        }



    }

//    public void joinProcess(SignUpRequest signUpRequest) {
//
//        String username = signUpRequest.getUsername();
//        String password = signUpRequest.getPassword();
//
//        Boolean isExist = userRepository.existsByUsername(username);
//
//        if (isExist) {
//
//            return;
//        }
//
//        UserEntity data = new UserEntity();
//
//        data.setUsername(username);
//        data.setPassword(bCryptPasswordEncoder.encode(password));
//        data.setRole("ROLE_ADMIN");
//
//        userRepository.save(data);
//    }






}
