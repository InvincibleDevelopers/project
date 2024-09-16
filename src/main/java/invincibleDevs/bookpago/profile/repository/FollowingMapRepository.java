package invincibleDevs.bookpago.profile.repository;

import invincibleDevs.bookpago.profile.model.FollowingMap;
import invincibleDevs.bookpago.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingMapRepository extends JpaRepository<FollowingMap, Long> {

    boolean existsByFollowerAndFollowee(Profile follower, Profile followee);
    void deleteByFollowerAndFollowee(Profile follower, Profile followee);
}
