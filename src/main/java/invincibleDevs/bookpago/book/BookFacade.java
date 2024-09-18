package invincibleDevs.bookpago.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookFacade {
    private final BookSearchService bookSearchService;

    public List<BookDTO> searchBooksResponse(String query, int page, int size) throws Exception {
        return bookSearchService.searchBooks(query, page, size);
    }
}
