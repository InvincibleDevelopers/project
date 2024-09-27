package invincibleDevs.bookpago.book;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.service.UserEntityService;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookFacade {

    private final BookService bookService;
    private final ProfileService profileService;
    private final UserEntityService userEntityService;


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
}
