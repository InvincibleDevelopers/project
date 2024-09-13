package invincibleDevs.bookpago.controller;

import invincibleDevs.bookpago.dto.BookDTO;
import invincibleDevs.bookpago.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookInfo(@PathVariable Long bookId) {
        try {
            // BookDTO 객체를 서비스에서 가져옴
            BookDTO bookDTO = bookService.getBookInfo(bookId);

            // BookDTO가 null인 경우 404 Not Found 반환
            if (bookDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 성공적으로 BookDTO를 가져온 경우 200 OK 반환
            return ResponseEntity.ok(bookDTO);
        } catch (Exception e) {
            // 예외가 발생한 경우 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
