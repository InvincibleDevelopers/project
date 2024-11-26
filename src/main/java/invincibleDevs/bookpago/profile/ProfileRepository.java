package invincibleDevs.bookpago.profile;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT p FROM Profile p WHERE p.userEntity.id = :userEntityId")
    Optional<Profile> findByUserEntityId(@Param("userEntityId") Long userEntityId);

    //연결된 UserEntity의 username으로찾기
    @Query("select p from Profile p where p.userEntity.kakaoId = :kakaoId")
    Optional<Profile> findByUserEntityUserKakaoId(@Param("kakaoId") Long kakaoId);

    Profile findByNickName(String nickname);

    @Query("select p from Profile p join fetch p.userEntity where p.nickName like %:keyword%")
    List<Profile> findByKeyword(@Param("keyword") String keyword);

    Optional<List<Profile>> findByIdIn(List<Long> ids);
}
