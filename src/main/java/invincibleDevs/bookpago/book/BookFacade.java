package invincibleDevs.bookpago.book;

import invincibleDevs.bookpago.Users.UserEntity;
import invincibleDevs.bookpago.Users.UserEntityService;
import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.profile.Profile;
import invincibleDevs.bookpago.review.Review;
import invincibleDevs.bookpago.review.ReviewRequest;
import invincibleDevs.bookpago.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookFacade {

    private final BookService bookService;
    private final ProfileService profileService;
    private final UserEntityService userEntityService;
    private final ReviewService reviewService;
    private final OpenAiChatModel openAiChatModel;


    public BookDetailDTO getBookInfoResponse(Long bookIsbn, Long kakaoId) {
        UserEntity user = userEntityService.findByKakaoId(kakaoId);
        Profile siteUser = profileService.findProfilebyUser(user);
        BookDetailDTO bookDetailDTO = bookService.getBookInfo(bookIsbn);
        if (profileService.existsIsbnInWishList(siteUser.getUserEntity().getKakaoId(), bookIsbn)) {
            bookDetailDTO.setWishBook(true);
        }

        return bookDetailDTO;
    }

    public BookSearchDTO searchBooksResponse(String query, int page, int size) throws Exception {
        return bookService.searchBooks(query, page, size);
    }

    public BookSearchDTO getBestsellersResponse() throws Exception {
        return bookService.getBestsellers();
    }

    public BookSearchDTO recommendBooks(Long kakaoId, int size) throws Exception {
        Profile profile = profileService.findByKakaoId(kakaoId);
        StringBuilder recommendBooks = new StringBuilder();

        List<Long> wishIsbnList = profile.getWishIsbnList();
        if (wishIsbnList != null) {
            for (Long wishIsbn : wishIsbnList) {
                BookDTO bookDTO = bookService.searchBook(wishIsbn.toString());
                String bookInfo = bookDTO.getTitle() + "(" + bookDTO.getAuthor() + ")";
                if (recommendBooks.isEmpty()) {
                    recommendBooks.append(bookInfo);
                } else {
                    recommendBooks.append(", ").append(bookInfo);
                }
            }
        }

        List<Review> reviewList = profile.getReviewList();
        if (reviewList != null) {
            for (Review review : reviewList) {
                if (review.getRating() > 3.0) {
                    BookDTO bookDTO = bookService.searchBook(review.getIsbn().toString());
                    if (bookDTO != null) {
                        String bookInfo = bookDTO.getTitle() + "(" + bookDTO.getAuthor() + ")";
                        if (recommendBooks.isEmpty()) {
                            recommendBooks.append(bookInfo);
                        } else {
                            recommendBooks.append(", ").append(bookInfo);
                        }
                    }
                }
            }
        }

        String prompt = null;
        if (recommendBooks.isEmpty()) {
            prompt = "사용자가 선호하는 책을 아직 모릅니다. 고전이나 오래된 문학 작품이 아닌 " + LocalDate.now().getYear()
                    + "년에 유행하고 평점이 높은 책을 " + size
                    + "개 추천해 주세요. 형식은 '1. 책 제목 (저자명)'과 같이 번호와 책 제목, 저자명을 나열해 주세요. 다른 추가 정보 없이 책 제목과 저자만 나열해 주세요.";
        } else {
            prompt = "사용자가 " + recommendBooks.toString()
                    + "과 같은 책을 좋아합니다. 고전이나 오래된 문학 작품이 아닌 이 책들과 유사하고 평가가 좋은 책을 " + size
                    + "개 추천해 주세요. 형식은 '1. 책 제목 (저자명)'과 같이 번호와 책 제목, 저자명을 나열해 주세요. 다른 추가 정보 없이 책 제목과 저자만 나열해 주세요.";
        }

        String openAiResponse = openAiChatModel.call(prompt);
        List<String> recommendList = bookService.extractRecommendations(openAiResponse);

        List<BookDTO> bookDTOList = new ArrayList<>();
        while (bookDTOList.size() < size) {
            for (String recommend : recommendList) {
                BookDTO bookDTO = bookService.searchBook(recommend);
                if (bookDTO != null) {
                    bookDTOList.add(bookDTO);
                    if (bookDTOList.size() == size) {
                        break;
                    }
                }
            }
            if (bookDTOList.size() < size) {
                String retryPrompt = "추천한 책 중 일부는 찾을 수 없었습니다. 추천한 책이 아니고 아까와 똑같은 조건에서 " + size
                        + "개의 책을 추천해 주세요. 형식은 '1. 책 제목 (저자명)'과 같이 번호와 책 제목, 저자명을 나열해 주세요.";
                openAiResponse = openAiChatModel.call(retryPrompt);
                recommendList = bookService.extractRecommendations(openAiResponse);
            }
        }

        return new BookSearchDTO(bookDTOList.size(), bookDTOList);
    }

    public String addWishBook(WishBookRequest wishBookRequest, Long isbn) {
        return profileService.addWishBook(wishBookRequest.kakaoId(), isbn);
    }

    public Long addReview(ReviewRequest reviewRequest) {
        Profile profile = profileService.findByKakaoId(reviewRequest.kakaoId());
        return reviewService.addReview(profile, reviewRequest).getId();
    }

    public String updateReview(ReviewRequest reviewRequest) {
        Profile profile = profileService.findByKakaoId(reviewRequest.kakaoId());
        reviewService.updateReview(profile, reviewRequest);
        return "ok";
    }

    public String deleteReview(ReviewRequest reviewRequest) {
        Profile profile = profileService.findByKakaoId(reviewRequest.kakaoId());
        reviewService.deleteReview(profile, reviewRequest);
        return "ok";
    }
}
