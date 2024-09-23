package invincibleDevs.bookpago.book;

import com.amazonaws.Response;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookFacade bookFacade;


    @GetMapping("/bestsellers")
    public ResponseEntity<?> getBestsellers() {
        try {
            return ResponseEntity.ok(bookFacade.getBestsellersResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{bookIsbn:\\d+}")
    public ResponseEntity<BookDetailDTO> getBookInfo(
            @ApiParam(value = "책 상세 정보", required = true)
            @PathVariable("bookIsbn") Long bookIsbn
    ) {
        try {
            return ResponseEntity.ok(bookFacade.getBookInfoResponse(bookIsbn));
        } catch (Exception e) {
            // 예외가 발생한 경우 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(
            @ApiParam(value = "책 검색 결과", required = true)
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            return ResponseEntity.ok(bookFacade.searchBooksResponse(query, page, size));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while searching for books.");
        }
    }

    @PostMapping("/likes")
    public ResponseEntity<?> addWishBook(
            @ApiParam(value = "책 좋아요", required = true)
            @RequestBody WishBookRequest wishBookRequest
            ){
        bookFacade.addWishBook(wishBookRequest);
        return ResponseEntity.ok("200");
    }
}
