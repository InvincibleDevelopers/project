package invincibleDevs.bookpago.book;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookFacade bookFacade;
    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookInfo(@PathVariable("bookId") Long bookId) {
        try {
            // BookDTO 객체를 서비스에서 가져옴
            BookDTO bookDTO = bookService.getBookInfo(bookId);

            // BookDTO가 null인 경우 404 Not Found 반환
            if (bookDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            System.out.println(bookId);
            // 성공적으로 BookDTO를 가져온 경우 200 OK 반환
            return ResponseEntity.ok(bookDTO);
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

}
