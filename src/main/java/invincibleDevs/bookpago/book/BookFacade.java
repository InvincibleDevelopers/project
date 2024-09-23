package invincibleDevs.bookpago.book;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookFacade {
    private final BookService bookService;
    private final ProfileService profileService;

    public BookDetailDTO getBookInfoResponse(Long bookIsbn) {
        return bookService.getBookInfo(bookIsbn);
    }
    public BookSearchDTO searchBooksResponse(String query, int page, int size) throws Exception {
        return bookService.searchBooks(query, page, size);
    }

    public BookSearchDTO getBestsellersResponse() throws Exception {
        return bookService.getBestsellers();
    }

    public void addWishBook(WishBookRequest wishBookRequest) {
//        Profile profile = profileService.findByNickname(wishBookRequest.nickname());
        profileService.addWishBook(wishBookRequest.nickname(), wishBookRequest.isbn());
//        bookService.addWishBook(profile, wishBookRequest.isbn());
    }
}
