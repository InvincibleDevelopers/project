package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.profile.model.Profile;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;

    //    public Page<ReviewDto> isLiked(Page<ReviewDto> bookReviews, Profile profile) {
//        List<ReviewDto> reviewList = bookReviews.getContent();
//
//        reviewLikeRepository.findByReviewIdInAndProfile(reviewDto.id());
//
//        for (ReviewDto reviewDto : reviewList) {
//
//        }
//
//    }
    public List<ReviewLike> reviewLikeList(List<Long> reviewIds, Profile profile) {
        // 리뷰 ID 리스트에 대해 좋아요 여부를 한 번에 조회합니다.
        List<ReviewLike> reviewLikes = reviewLikeRepository.findAllByReviewIdInAndProfile(
                reviewIds, profile);

        return reviewLikes;
    }

    public List<ReviewDto> reviewLikeMap(List<ReviewLike> reviewLikes, Page<Review> reviews) {
        // 좋아요 여부를 리뷰 ID를 키로 하는 맵으로 변환합니다.
        Map<Long, Boolean> reviewLikesMap = reviewLikes.stream()
                                                       .collect(Collectors.toMap(
//                                                               ReviewLike::getReview.getId,
                                                               rl -> rl.getReview().getId(),
                                                               rl -> true));

        // 리뷰 리스트를 돌며 좋아요 여부를 추가합니다.
        List<ReviewDto> reviewDtos = reviews.stream()
                                            .map(review -> {
                                                boolean isLiked = reviewLikesMap.getOrDefault(
                                                        review.getId(), false);
                                                System.out.println(isLiked);
                                                return new ReviewDto(
                                                        review.getId(),
                                                        review.getRating(),
                                                        review.getContent(),
                                                        review.getProfile().getNickName(),
                                                        Optional.of(isLiked));
                                            })
                                            .collect(Collectors.toList());
        return reviewDtos;
    }

    public String addLikes(Review review, Profile profile) {
        if (existsByIdAndProfile(review, profile)) { // 이미 존재할경우 좋아요 해제
            ReviewLike reviewLike = reviewLikeRepository.findByReviewAndProfile(review, profile);
            reviewLikeRepository.delete(reviewLike);
            return "deleteLikes";
        }
        ReviewLike reviewLike = ReviewLike.builder()
                                          .review(review)
                                          .profile(profile)
                                          .build();
        reviewLikeRepository.save(reviewLike);
        return "addLikes";
    }

    public boolean existsByIdAndProfile(Review r, Profile p) {
        return reviewLikeRepository.existsByReviewAndProfile(r, p);
    }
}
