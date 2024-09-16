package invincibleDevs.bookpago.profile.model;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@Builder(toBuilder = true)  // toBuilder = true 설정
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserEntity userEntity;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String profileImgUrl;

    @Builder.Default
    @Column(nullable = false)
    private String nickName = "User";

    @Builder.Default
    private String introduce = "";

//    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE )
//    private List<Review> reviewList;
//
    @OneToMany(mappedBy = "clubAdmin") // BookClub의 admin 필드와 매핑
    private Set<ReadingClubMap> managedClub; // 관리하는 독서 모임

    @OneToMany(mappedBy = "clubMember")
    private Set<ReadingClubMap> readingClubs; // 가입한 독서 모임

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE )
    private Set<FollowingMap> followerMaps;

    @OneToMany(mappedBy = "followee")
    private Set<FollowingMap> followeeMaps;


//▼▼▼▼ DM기능 추가시 활성화 예정. 삭제 금지 ▼▼▼

//    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE )
//    private List<Message> myMessages;
//
//    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE )
//    private List<Message> receivedMessages;
//
//    @OneToMany(mappedBy = "me",cascade = CascadeType.REMOVE)
//    private List<DmPage> myDmpageList;
//
//    @OneToMany(mappedBy = "partner")
//    private List<DmPage> otherDmpageList;
}
