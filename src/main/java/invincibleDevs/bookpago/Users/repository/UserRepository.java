package invincibleDevs.bookpago.Users.repository;

import invincibleDevs.bookpago.Users.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByKakaoId(Long kakaoId);

    UserEntity findByKakaoId(Long kakaoId);
}
