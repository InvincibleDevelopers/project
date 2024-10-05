package invincibleDevs.bookpago.book;

import invincibleDevs.bookpago.review.ReviewRequest;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getBookInfo( //이 api호출엔 항상 요청자 정보받아야됨
            @ApiParam(value = "책 상세 정보", required = true)
            @PathVariable("bookIsbn") Long bookIsbn,
            @ApiParam(value = "kakaoId", required = true) // 설명 추가
            @RequestParam(name = "kakaoId") Long kakaoId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            return ResponseEntity.ok(bookFacade.getBookInfoResponse(bookIsbn, kakaoId, page, size));
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while searching for books.");
        }
    }

    @PostMapping("/{isbn}/likes")
    public ResponseEntity<?> addWishBook(
            @ApiParam(value = "책 좋아요", required = true)
            @PathVariable("isbn") Long isbn,
            @RequestBody WishBookRequest wishBookRequest
    ) {
        return ResponseEntity.ok(bookFacade.addWishBook(wishBookRequest, isbn));
    }

    @PostMapping("/{bookIsbn:\\d+}")
    public ResponseEntity<?> addReview( //이 api호출엔 항상 요청자 정보받아야됨
            @ApiParam(value = "책 상세 정보", required = true)
            @PathVariable("bookIsbn") Long bookIsbn,
            @RequestBody ReviewRequest reviewRequest
    ) {
        try {
            return ResponseEntity.ok("reviewId : " + bookFacade.addReview(reviewRequest));
        } catch (Exception e) {
            // 예외가 발생한 경우 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{bookIsbn:\\d+}")
    public ResponseEntity<?> updateReview( //이 api호출엔 항상 요청자 정보받아야됨
            @ApiParam(value = "책 상세 정보", required = true)
            @PathVariable("bookIsbn") Long bookIsbn,
            @RequestBody ReviewRequest reviewRequest
    ) {
        try {
            return ResponseEntity.ok(bookFacade.updateReview(reviewRequest));
        } catch (Exception e) {
            // 예외가 발생한 경우 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{bookIsbn:\\d+}")
    public ResponseEntity<?> deleteReview( //이 api호출엔 항상 요청자 정보받아야됨
            @ApiParam(value = "책 상세 정보", required = true)
            @PathVariable("bookIsbn") Long bookIsbn,
            @RequestBody ReviewRequest reviewRequest
    ) {
        try {
            return ResponseEntity.ok(bookFacade.deleteReview(reviewRequest));
        } catch (Exception e) {
            // 예외가 발생한 경우 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
