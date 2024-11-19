package invincibleDevs.bookpago.mapper.repository;

import invincibleDevs.bookpago.mapper.model.FollowingMap;
import invincibleDevs.bookpago.profile.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowingMapRepository extends JpaRepository<FollowingMap, Long> {

    boolean existsByFollowerAndFollowee(Profile follower, Profile followee);

    void deleteByFollowerAndFollowee(Profile follower, Profile followee);

    @Query("SELECT fm.follower FROM FollowingMap fm WHERE fm.followee.id = :profileId ORDER BY fm.id DESC")
    Page<Profile> findFollowersByProfileId(@Param("profileId") Long profileId, Pageable pageable);

    @Query("SELECT fm.followee FROM FollowingMap fm WHERE fm.follower.id = :profileId ORDER BY fm.id DESC")
    Page<Profile> findFollowingsByProfileId(@Param("profileId") Long profileId, Pageable pageable);
}
