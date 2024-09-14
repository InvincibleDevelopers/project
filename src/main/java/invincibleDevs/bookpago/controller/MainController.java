package invincibleDevs.bookpago.controller;

import invincibleDevs.bookpago.Users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;

@RestController
@ResponseBody
@RequiredArgsConstructor
public class MainController {
    private final UsersService usersService;
//    @GetMapping("/")
//    public String mainP() {
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
//        GrantedAuthority auth = iter.next();
//        String role = auth.getAuthority();
//
//        return "Main Controller" + username + role;
//    }

    @GetMapping("/")
    public ResponseEntity<String> mainP() {
        // 현재 인증된 사용자의 이름을 가져옴
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 현재 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자 권한 정보를 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = null;

        // 사용자 권한이 존재하는 경우 첫 번째 권한을 가져옴
        if (!authorities.isEmpty()) {
            Iterator<? extends GrantedAuthority> iter = authorities.iterator();
            GrantedAuthority auth = iter.next();
            role = auth.getAuthority();
        }

        // 사용자 이름과 역할 정보를 포함한 메시지 생성
        String message = "Main Controller: " + username + " - Role: " + (role != null ? role : "No Role");
        System.out.println("=====================tetst=========================");


        // 메시지를 담아 200 OK 응답 반환
        return ResponseEntity.ok(message);
    }



}
