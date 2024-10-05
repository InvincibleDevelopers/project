package invincibleDevs.bookpago.book;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.service.UserEntityService;
import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.review.ReviewRequest;
import invincibleDevs.bookpago.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookFacade {

    private final BookService bookService;
    private final ProfileService profileService;
    private final UserEntityService userEntityService;
    private final ReviewService reviewService;


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
