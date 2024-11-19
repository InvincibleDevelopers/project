package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.profile.Profile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {


    List<ReviewLike> findAllByReviewIdInAndProfile(List<Long> reviewIds, Profile profile);

    boolean existsByReviewAndProfile(Review r, Profile p);

    ReviewLike findByReviewAndProfile(Review review, Profile profile);

    // 주어진 리뷰 리스트에 해당하는 ReviewLikes 가져오기
    List<ReviewLike> findByReviewIn(List<Review> reviewList);

    boolean existsByProfileAndReview(Profile profile, Review review);

}
