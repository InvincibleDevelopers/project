package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.book.Book;
import invincibleDevs.bookpago.book.BookDTO;
import invincibleDevs.bookpago.profile.MyBookDto;
import invincibleDevs.bookpago.profile.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.origin.PropertySourceOrigin;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
            throw new IllegalArgumentException("Unsupported type for conversion to Long: " + obj.getClass().getName());
        }
    }

    public List<Review> getMyReviews(Profile profile, Long lastBookId, int size) {
        if (lastBookId == null) {
           return reviewRepository.findFirstReviewsByLastBookIsbn(profile.getId(), size);
        } else {
            return reviewRepository.findReviewsByLastBookIsbnAfterCursor(profile.getId(), lastBookId,size);
        }
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
