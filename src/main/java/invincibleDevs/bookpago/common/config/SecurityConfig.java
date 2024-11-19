package invincibleDevs.bookpago.common.config;

import invincibleDevs.bookpago.Users.UserRepository;
import invincibleDevs.bookpago.common.CustomOAuth2UserService;
import invincibleDevs.bookpago.common.CustomUserDetailsService;
import invincibleDevs.bookpago.common.JWTFilter;
import invincibleDevs.bookpago.common.JWTUtil;
import invincibleDevs.bookpago.common.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    // 모든 필드를 주입받는 생성자
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil,
            UserRepository userRepository, CustomUserDetailsService customUserDetailsService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository; // UserRepository 주입
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(
                                    HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();
                                configuration.setAllowedOrigins(
                                        Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);
                                configuration.setExposedHeaders(
                                        Collections.singletonList("Authorization"));
                                return configuration;
                            }
                        }));

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/oauth2/authorization/kakao")
                        .permitAll() // 카카오 경로 인증 비활성화
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/kakao")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/login?error")
                        .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService())
                                // CustomOAuth2UserService 사용
                        ))
                .logout(logout -> logout.logoutSuccessUrl("/"));

        http
                .addFilterBefore(new JWTFilter(jwtUtil, customUserDetailsService),
                        LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),
                        jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new CustomOAuth2UserService(
                userRepository); // UserRepository를 사용하여 CustomOAuth2UserService 생성
    }
}
