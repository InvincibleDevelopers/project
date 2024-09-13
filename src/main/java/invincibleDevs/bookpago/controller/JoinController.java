package invincibleDevs.bookpago.controller;

import invincibleDevs.bookpago.dto.JoinDTO;
import invincibleDevs.bookpago.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ResponseBody
public class JoinController {
    private final JoinService joinService;

//    @PostMapping("/join")
//    public String joinProcess(@RequestBody JoinDTO joinDTO) {
//        joinService.joinProcess(joinDTO);
//
//        return "ok";
//    }

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        try {
            // 회원 가입 처리
            joinService.joinProcess(joinDTO);

            // 성공적으로 처리된 경우 200 OK와 메시지 반환
            return ResponseEntity.ok("회원 가입이 성공적으로 처리되었습니다.");
        } catch (Exception e) {
            // 예외가 발생한 경우 500 Internal Server Error와 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 가입 처리 중 오류가 발생했습니다.");
        }
    }

}
