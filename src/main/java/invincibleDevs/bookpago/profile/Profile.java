package invincibleDevs.bookpago.profile;

import invincibleDevs.bookpago.Users.model.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserEntity userEntity;

    @Builder.Default
    private String profileImgUrl = "https://s3.amazonaws.com/e7c257aa-5b1d-48a1-aece-93eec0965365_profile_sample.png";

    @Builder.Default
    @Column(unique = true, nullable = false)
    private String nickName = "User";

    @Builder.Default
    private String introduce = "";

//    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE )
//    private List<Review> reviewList;
//
//    @OneToMany(mappedBy = "profile", cascade = CascadeType.REMOVE)
//    private List<ReadingClub> readingClubList;
//
//    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE )
//    private Set<FollowingMap> followerMaps;
//
//    @OneToMany(mappedBy = "followee")
//    private Set<FollowingMap> followeeMaps;


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
