package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.profile.model.Profile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;
    private final ProfileService profileService;

    public List<ReviewDto> getReviews(Long kakaoId, Long bookIsbn, int page, int size) {
        // 리뷰 페이징 처리해서 가져오기
        Profile profile = profileService.findByKakaoId(kakaoId);
        Page<Review> reviews = reviewService.getBookReviews(bookIsbn, page, size);
        List<Long> reviewIds = reviewService.getBookReviewsIdList(bookIsbn, page, size);
        List<ReviewLike> reviewLikeList = reviewLikeService.reviewLikeList(reviewIds, profile);

        return reviewLikeService.reviewDtoList(reviewLikeList, reviews,
                reviewLikeService.countLikes(reviews));
    }

    public String addLikes(ReviewLikeRequest reviewLikeRequest) {
        Review review = reviewService.getReview(reviewLikeRequest.reviewId());
        Profile profile = profileService.findByKakaoId(reviewLikeRequest.kakaoId());
        return reviewLikeService.addLikes(review, profile);
    }


}
