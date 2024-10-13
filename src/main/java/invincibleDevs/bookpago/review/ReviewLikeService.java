package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.profile.model.Profile;
import java.util.LinkedHashMap;
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

    public List<ReviewDto> reviewDtoList(List<ReviewLike> reviewLikes, Page<Review> reviews,
            Map<Long, Integer> countLikes) {
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
                                                // Map에서 reviewId를 기준으로 좋아요 갯수를 찾아 반환, 없으면 0 반환
                                                int likeCount = countLikes.getOrDefault(
                                                        review.getId(), 0);
                                                return new ReviewDto(
                                                        review.getId(),
                                                        review.getRating(),
                                                        review.getContent(),
                                                        review.getProfile().getNickName(),
                                                        review.getProfile().getProfileImgUrl(),
                                                        Optional.of(isLiked),
                                                        likeCount);//좋아요갯수넣어야댐
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

    public Map<Long, Integer> findMostLikedReviewMap(List<Review> reviewList) {
        List<ReviewLike> reviewLikeList = reviewLikeRepository.findByReviewIn(reviewList);

        // 각 리뷰의 좋아요 수를 count하고 review_id로 정렬
        Map<Long, Integer> reviewLikeCountMap = reviewLikeList.stream()
                                                              .collect(Collectors.groupingBy(
                                                                      reviewLike -> reviewLike.getReview()
                                                                                              .getId(),
                                                                      // ReviewLike에서 review_id 가져오기
                                                                      LinkedHashMap::new,
                                                                      // 순서를 유지하기 위해 LinkedHashMap 사용
                                                                      Collectors.collectingAndThen(
                                                                              Collectors.counting(),
                                                                              Long::intValue
                                                                      )
                                                              ));

        // 가장 많은 좋아요를 받은 리뷰 ID 찾기
        Optional<Map.Entry<Long, Integer>> maxEntry = reviewLikeCountMap.entrySet()
                                                                        .stream()
                                                                        .max(Map.Entry.comparingByValue()); // 값(Long) 기준으로 max 찾기

        // maxEntry가 있을 경우 그 리뷰 ID를 반환, 없을 경우 -1 반환
        Long mostLikedReviewId = maxEntry.map(Map.Entry::getKey).orElse(null);
        return reviewLikeCountMap;
    }


    public Map<Long, Integer> countLikes(Page<Review> reviews) {
        List<Review> reviewList = reviews.getContent();
        List<ReviewLike> reviewLikeList = reviewLikeRepository.findByReviewIn(reviewList);

        // 각 리뷰의 좋아요 수를 count하고 review_id로 정렬
        Map<Long, Integer> reviewLikeCountMap = reviewLikeList.stream()
                                                              .collect(Collectors.groupingBy(
                                                                      reviewLike -> reviewLike.getReview()
                                                                                              .getId(),
                                                                      // ReviewLike에서 review_id 가져오기
                                                                      LinkedHashMap::new,
                                                                      // 순서를 유지하기 위해 LinkedHashMap 사용
                                                                      Collectors.collectingAndThen(
                                                                              Collectors.counting(),
                                                                              Long::intValue
                                                                      )
                                                              ));
        return reviewLikeCountMap;

    }

    public boolean booleanByProfileReview(Profile currentProfile, Review review) {
        return reviewLikeRepository.existsByProfileAndReview(currentProfile, review);
    }
}
