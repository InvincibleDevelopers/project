package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.profile.model.Profile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 안전하게 Number 타입을 Long으로 변환하는 메서드
    private Long convertToLong(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();  // Integer -> Long 변환
        } else if (obj instanceof Long) {
            return (Long) obj;  // 이미 Long이면 그대로 반환
        } else {
            throw new IllegalArgumentException(
                    "Unsupported type for conversion to Long: " + obj.getClass().getName());
        }
    }

    public Page<ReviewDto> getBookReviews(Long isbn, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviews = reviewRepository.findByIsbn(isbn, pageable);

        return reviews.map(review -> new ReviewDto(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getProfile().getNickName()
        ));
    }

    public List<Review> getMyReviews(Profile profile, Long lastBookId, int size) {
        if (lastBookId == null) {
            return reviewRepository.findFirstReviewsByLastBookIsbn(
                    profile.getUserEntity().getKakaoId(), size);
        } else {
            return reviewRepository.findReviewsByLastBookIsbnAfterCursor(
                    profile.getUserEntity().getKakaoId(),
                    lastBookId, size);
        }
    }

    public Review addReview(Profile profile, ReviewRequest reviewRequest) {
        Review review = Review.builder()
                              .rating(reviewRequest.rating())
                              .content(reviewRequest.content())
                              .isbn(reviewRequest.isbn())
                              .profile(profile)
                              .build();
        return reviewRepository.save(review);
    }

    public Review updateReview(Profile profile, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewRequest.reviewId()
                                                               .orElseThrow())
                                        .orElseThrow();
        Review updatedReview = review.toBuilder()
                                     .rating(reviewRequest.rating())
                                     .content(reviewRequest.content())
                                     .build();
        return reviewRepository.save(updatedReview);
    }

    public void deleteReview(Profile profile, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewRequest.reviewId()
                                                               .orElseThrow())
                                        .orElseThrow();
        reviewRepository.delete(review);
    }

//    public List<BookDTO> getMyWishBooks(Profile profile, Long lastBookId, int size) {
//        List<Long> myWishBookisbn = profile.getWishIsbnList();
//        if (lastBookId == null) {
//            // 처음 페이지인 경우, 커서 없이 처음부터 조회
//            List<BookDTO> wishBookDtoList = new ArrayList<>();
//            return wishBookDtoList.stream()
//                    .map(myWishBookisbn -> new wishBookDtoList(
//                            result.getId(),          // id는 Review 객체에서 직접 참조
//                            result.getContent(),     // content는 Review 객체에서 참조
//                            result.getIsbn(),        // isbn은 Review 객체에서 참조
//                            result.getRating(),      // rating은 Review 객체에서 참조
//                            result.getProfile().getId() // authorId는 Review 객체의 authorProfile에서 참조
//                    ))
//                    .collect(Collectors.toList());
//
//        } else {
//            // 커서가 있는 경우, 해당 ID 이후의 데이터를 조회
//            List<BookDTO> results = reviewRepository.findBooksByLastBookIdAfterCursor(profile.getId(), lastBookId,size);
//            return results.stream()
//                    .map(result -> new MyReviewDto(
//                            result.getId(),          // id는 Review 객체에서 직접 참조
//                            result.getContent(),     // content는 Review 객체에서 참조
//                            result.getIsbn(),        // isbn은 Review 객체에서 참조
//                            result.getRating(),      // rating은 Review 객체에서 참조
//                            result.getProfile().getId() // authorId는 Review 객체의 authorProfile에서 참조
//                    ))
//                    .collect(Collectors.toList());
//        }
//    }

}
