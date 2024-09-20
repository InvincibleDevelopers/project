package invincibleDevs.bookpago.profile.repository;

import invincibleDevs.bookpago.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p WHERE p.userEntity.id = :userEntityId")
    Optional<Profile> findByUserEntityId(@Param("userEntityId") Long userEntityId);
    //연결된 UserEntity의 username으로찾기
    @Query("select p from Profile p where p.userEntity.username = :username")
    Optional<Profile> findByUserEntityUserName(@Param("username") String username);

}
