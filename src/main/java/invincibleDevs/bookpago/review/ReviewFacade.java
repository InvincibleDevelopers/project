package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.profile.model.Profile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;
    private final ProfileService profileService;
    private final CommentService commentService;

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

    public ResponseEntity<?> addComment(CommentRequest commentRequest) {
        try {
            Profile profile = profileService.findByKakaoId(commentRequest.profileId());
            Review review = reviewService.getReview(commentRequest.reviewId()
                                                                  .orElseThrow(
                                                                          () -> new CustomException(
                                                                                  "ReviewId Is Required.")));
            commentService.addComment(review, profile, commentRequest.content()
                                                                     .orElseThrow(
                                                                             () -> new CustomException(
                                                                                     "Content Is Required.")));
            return ResponseEntity.ok("success");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    public ResponseEntity<?> updateComment(CommentRequest commentRequest) {
        try {
            Profile profile = profileService.findByKakaoId(commentRequest.profileId());
            Comment comment = commentService.findById(commentRequest.commentId()
                                                                    .orElseThrow(
                                                                            () -> new CustomException(
                                                                                    "CommentId Is Required.")
                                                                    ));
            if (commentService.isAuthorOfComment(comment, profile)) {
                commentService.updateComment(comment, commentRequest.content()
                                                                    .orElseThrow(
                                                                            () -> new CustomException(
                                                                                    "Content Is Required.")));
                return ResponseEntity.ok("success");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("Comment's AuthorId does not match.");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteComment(CommentRequest commentRequest) {
        try {
            Profile profile = profileService.findByKakaoId(commentRequest.profileId());
            Comment comment = commentService.findById(commentRequest.commentId()
                                                                    .orElseThrow(
                                                                            () -> new CustomException(
                                                                                    "CommentId Is Required.")
                                                                    ));
            if (commentService.isAuthorOfComment(comment, profile)) {
                commentService.deleteComment(comment);
                return ResponseEntity.ok("success");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("Comment's AuthorId does not match.");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
