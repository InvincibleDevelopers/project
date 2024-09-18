package invincibleDevs.bookpago.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookFacade {
    private final BookService bookService;

    public BookDetailDTO getBookInfoResponse(Long bookIsbn) {
        return bookService.getBookInfo(bookIsbn);
    }
    public BookSearchDTO searchBooksResponse(String query, int page, int size) throws Exception {
        return bookService.searchBooks(query, page, size);
    }
}
