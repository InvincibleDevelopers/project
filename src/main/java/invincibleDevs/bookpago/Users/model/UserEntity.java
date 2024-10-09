package invincibleDevs.bookpago.Users.model;

import invincibleDevs.bookpago.profile.model.Profile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long kakaoId;

    @Column(nullable = false)
    @Builder.Default
    private String password = "";

    @Column(nullable = false)
    private String nickname;

    @Builder.Default
    private String role = "USER";

    private String gender;

    private int age;

    private LocalDateTime created_at;

    // 연동 프로필
    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private Profile profile;

//    // 기본 생성자: Hibernate 사용을 위해 protected 또는 public으로 설정
//    // 빌더 생성자
//    private UserEntity(Builder builder) {
//        this.username = builder.username;
//        this.password = builder.password;
//        this.role = builder.role;
//    }
//
//    // 빌더 패턴 구현
//    public static class Builder {
//        private String username;
//        private String password;
//        private String role;
//
//        public Builder username(String username) {
//            this.username = username;
//            this.password = password;
//            this.role = role;
//            return this;
//        }
//    // sns 연동 로그인에 사용되는 provider 종류(GOOGLE, NAVER, KAKAO)
//    private String provider;
//
//    // sns 연동 로그인에 사용되는 provider ID
//    private String providerId;

    //▼▼▼▼ DM기능 추가시 활성화 예정. 삭제 금지 ▼▼▼
//    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
//    private Set<ChatMessage> sendMessage = new HashSet<>();
//
//    @OneToMany(mappedBy = "admin", cascade = CascadeType.REMOVE)
//    private Set<ChatRoom> adminChatRooms = new HashSet<>();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
//    private Set<MemberChatRoom> memberChatRooms = new HashSet<>();

}

