package invincibleDevs.bookpago.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    private String gender;

    private int age;

    private LocalDateTime created_at;

    // 연동 프로필
    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private Profile profile;

    // sns 연동 로그인에 사용되는 provider 종류(GOOGLE, NAVER, KAKAO)
    private String provider;

    // sns 연동 로그인에 사용되는 provider ID
    private String providerId;

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
