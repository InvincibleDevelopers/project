package invincibleDevs.bookpago.Users.repository;

import invincibleDevs.bookpago.Users.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByUsername(String username);
    UserEntity findByUsername(String username);
}
