package invincibleDevs.bookpago.model;

import invincibleDevs.bookpago.profile.Profile;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @Builder.Default
    private String password = "password";

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
