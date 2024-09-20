package invincibleDevs.bookpago.mapper.repository;

import invincibleDevs.bookpago.mapper.model.ReviewLikesMap;
import invincibleDevs.bookpago.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikesMapRepository extends JpaRepository<ReviewLikesMap, Long> {

}
