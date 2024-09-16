package invincibleDevs.bookpago.profile.model;

import invincibleDevs.bookpago.profile.model.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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