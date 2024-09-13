package invincibleDevs.bookpago.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserEntity userEntity;

    @OneToOne(mappedBy = "profileImage", cascade = CascadeType.REMOVE)
    private Image profileImage;

    @Column(unique = true, nullable = false)
    private String nickName;

    private String bio;

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE )
    private List<Review> reviewList;



    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE )
    private Set<FollowingMap> followerMaps;

    @OneToMany(mappedBy = "followee")
    private Set<FollowingMap> followeeMaps = new HashSet<>();


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
