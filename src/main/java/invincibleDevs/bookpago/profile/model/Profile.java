package invincibleDevs.bookpago.profile.model;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.mapper.model.FollowingMap;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
import invincibleDevs.bookpago.review.Review;
import invincibleDevs.bookpago.review.ReviewLike;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)  // toBuilder = true 설정
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class Profile {

    //    @Id
//    private Long kakaoId;  // userEntity의 kakaoId를 기본 키로 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "kakaoId", referencedColumnName = "kakaoId")
    // Profile 테이블의 kakao_id가 UserEntity의 kakaoId를 참조
    private UserEntity userEntity;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String profileImgUrl;

    @Builder.Default
    @Column(nullable = false)
    private String nickName = "User";

    @Builder.Default
    private String introduce = "";

    @ElementCollection
    private List<Long> wishIsbnList;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.REMOVE)
    private List<Review> reviewList;

//    @OneToMany(mappedBy = "clubAdmin") // BookClub의 admin 필드와 매핑
//    private Set<ReadingClubMembers> managedClub; // 관리하는 독서 모임

    @OneToMany(mappedBy = "clubMember")
    private Set<ReadingClubMembers> readingClubs; // 가입한 독서 모임

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE)
    private Set<FollowingMap> followerMaps;

    @OneToMany(mappedBy = "followee")
    private Set<FollowingMap> followeeMaps;

    @OneToMany(mappedBy = "profile")
    private List<ReviewLike> reviewLikesMaps;

//    @OneToOne
//    private Calendar calendar;

//    @OneToMany(mappedBy = "me",cascade = CascadeType.REMOVE)
//    private List<ChatRoom> myChatRoomList;
//
//    @OneToMany(mappedBy = "partner")
//    private List<ChatRoom> otherChatRoomList;

//▼▼▼▼ DM기능 추가시 활성화 예정. 삭제 금지 ▼▼▼

//    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE )
//    private List<Message> myMessages;
//
//    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE )
//    private List<Message> receivedMessages;
//

}
