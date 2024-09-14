//package invincibleDevs.bookpago.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import invincibleDevs.bookpago.Users.dto.request.SignUpRequest;
//import invincibleDevs.bookpago.Users.dto.KakaoUserDTO;
//import invincibleDevs.bookpago.dto.OauthToken;
//import invincibleDevs.bookpago.service.JoinService;
//import invincibleDevs.bookpago.Users.service.UsersService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequiredArgsConstructor
//public class Oauth2Controller {
//    private final UsersService usersService;
//    private final JoinService joinService;
//    @GetMapping("/kakaoOauth") //인가코드받고 엑세스토큰 요청받고, 액세스토큰으로 회원정보까지 조회해서 결과 반환
//    public String kakaoCallback(@RequestParam("code") String code) {
//        RestTemplate rt = new RestTemplate();
//        //HttpHeader오브젝트 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//        //HttpBody 오브젝트 생성
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", "c4e03b31c7774ef543190a6266435331");
//        params.add("redirect_uri", "http://52.79.225.186:8088/kakaoOauth");
//        params.add("code", code);
//        //header와body를 하나의 오브젝트에 담기 --> 밑에 exchange라는 메서드가, httpEntity라는 오브젝트를 인수로 받기때문
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
//                new HttpEntity<>(params, headers);
//        //http요청하기 post방식, response변수의 응답 반음.
//        ResponseEntity<String> response = rt.exchange(
//                "https://kauth.kakao.com/oauth/token", //post요청보낼 주소
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        //받은 응답 오브젝트로 담기. Json데이터를 자바 오브젝트에서 처리하기 위해서.
//        // 라이브러리 사용 -> Gson,Json Simple, ObjectMapper등등, 마지막거씀.
//        ObjectMapper objectMapper = new ObjectMapper();
//        OauthToken oauthToken = new OauthToken();
//
//        try { //parsing할때 오류 막기위해 try catch로 감싸야함.
//            oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        RestTemplate rt2 = new RestTemplate();
//        HttpHeaders headers2 = new HttpHeaders();
//        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
//        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
//                new HttpEntity<>(headers2);
//
//        ResponseEntity<String> response2 = rt2.exchange(
//                "https://kapi.kakao.com/v2/user/me", //post요청보낼 주소
//                HttpMethod.POST,
//                kakaoProfileRequest,
//                String.class
//        );
//
//        ObjectMapper objectMapper2 = new ObjectMapper();
//        KakaoUserDTO kakaoUserDTO = new KakaoUserDTO();
//
//        try { //parsing할때 오류 막기위해 try catch로 감싸야함.
//            kakaoUserDTO = objectMapper2.readValue(response2.getBody(), KakaoUserDTO.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        if (usersService.isUserExists(kakaoUserDTO.getId())) {
//            //로그인처리
//        } else {
//            SignUpRequest signUpRequest = new SignUpRequest();
//            signUpRequest.setUsername(kakaoUserDTO.getId());
//            signUpRequest.setPassword("kakao-"+kakaoUserDTO.getId());
//            joinService.joinProcess(signUpRequest);
//        }
//
//
//        return response2.getBody();
//    }
//
//
//
//    @GetMapping("/hello")
//    public String testtest(){
//        System.out.println("==============teststt=========================");
//        return "hello world";
//    }
//}
