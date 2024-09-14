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

//    @OneToOne(mappedBy = "profileImage", cascade = CascadeType.REMOVE)
//    @Builder.Default
//    private Image profileImage = "...";

//    @Column(unique = true, nullable = false)
//    private String nickName;
//
//    @Builder.Default
//    private String bio = "";
//
//    private LocalDateTime modifyDate;
//
//    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE )
//    private List<Review> reviewList;
//
//
//
//    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE )
//    private Set<FollowingMap> followerMaps;
//
//    @OneToMany(mappedBy = "followee")
//    private Set<FollowingMap> followeeMaps = new HashSet<>();


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
