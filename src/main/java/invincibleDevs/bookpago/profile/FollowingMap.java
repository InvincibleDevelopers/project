package invincibleDevs.bookpago.profile;

import invincibleDevs.bookpago.profile.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "following_map", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followee_id"}))
public class FollowingMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Profile follower;

    @ManyToOne
    private Profile followee;

}