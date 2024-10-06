package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.profile.model.Profile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {


    List<ReviewLike> findAllByReviewIdInAndProfile(List<Long> reviewIds, Profile profile);

    boolean existsByReviewAndProfile(Review r, Profile p);

    ReviewLike findByReviewAndProfile(Review review, Profile profile);
}
