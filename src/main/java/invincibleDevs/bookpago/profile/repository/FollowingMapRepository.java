package invincibleDevs.bookpago.profile.repository;

import invincibleDevs.bookpago.profile.model.FollowingMap;
import invincibleDevs.bookpago.profile.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowingMapRepository extends JpaRepository<FollowingMap, Long> {

    boolean existsByFollowerAndFollowee(Profile follower, Profile followee);

    void deleteByFollowerAndFollowee(Profile follower, Profile followee);


    @Query("SELECT fm.follower FROM FollowingMap fm WHERE fm.followee.nickName = :nickname ORDER BY fm.follower.nickName ASC")
    Page<Profile> findFollowersByFollowee(@Param("nickname") String nickname, Pageable pageable);

}
