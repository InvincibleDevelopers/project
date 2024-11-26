package invincibleDevs.bookpago.Users;

import invincibleDevs.bookpago.Users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByKakaoId(Long kakaoId);

    UserEntity findByKakaoId(Long kakaoId);
}
