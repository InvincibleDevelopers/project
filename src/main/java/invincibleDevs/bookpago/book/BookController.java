package invincibleDevs.bookpago.book;

import com.fasterxml.jackson.databind.JsonNode;
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
