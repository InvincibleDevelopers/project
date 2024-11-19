package invincibleDevs.bookpago.common;

import invincibleDevs.bookpago.Users.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService; // 수정된 필드

    // 수정된 생성자
    public JWTFilter(JWTUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        System.out.println("*****************");
        System.out.println(request.getRequestURI());

        // 카카오 인증 요청에 대해 JWT 필터를 무시
        if (request.getRequestURI().equals("/user/kakaologin")) {
            System.out.println("카카오로그인JWT필터적용안함");
            filterChain.doFilter(request, response);
            return;
        }

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.substring(7);

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            filterChain.doFilter(request, response);

            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //userEntity를 생성하여 값 set
        UserEntity userEntity = UserEntity.builder()
                                          .nickname(username)
                                          .role(role)
                                          .build();

//        userDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

//        스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());

//        세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
