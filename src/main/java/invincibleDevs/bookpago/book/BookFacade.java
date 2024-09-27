package invincibleDevs.bookpago.book;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.service.ProfileService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookFacade {

    private final BookService bookService;
    private final ProfileService profileService;

    public Map<String, Object> getBookInfoResponse(Long bookIsbn, String nickname) {
        Profile siteUser = profileService.findByNickname(nickname);
        String isWishBook = "좋아요 안누른 사용자.";
        if (profileService.existsIsbnInWishList(siteUser.getId(), bookIsbn)) {
            isWishBook = "좋아요 누른 사용자 입니다.";
        }
        BookDetailDTO bookDetailDTO = bookService.getBookInfo(bookIsbn);
        // Map을 사용해 DTO와 String을 묶어서 반환
        Map<String, Object> response = new HashMap<>();
        response.put("bookDetail", bookDetailDTO);
        response.put("isWishBook", isWishBook);
        return response;
    }

    public BookSearchDTO searchBooksResponse(String query, int page, int size) throws Exception {
        return bookService.searchBooks(query, page, size);
    }

    public BookSearchDTO getBestsellersResponse() throws Exception {
        return bookService.getBestsellers();
    }

    public String addWishBook(WishBookRequest wishBookRequest, Long isbn) {
        return profileService.addWishBook(wishBookRequest.nickname(), isbn);
    }
}
