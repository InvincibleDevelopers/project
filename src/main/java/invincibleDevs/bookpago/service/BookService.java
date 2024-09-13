package invincibleDevs.bookpago.service;

import com.amazonaws.services.kms.model.NotFoundException;
import invincibleDevs.bookpago.dto.BookDTO;
import invincibleDevs.bookpago.model.Book;
import invincibleDevs.bookpago.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public BookDTO getBookInfo(Long bookId) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + bookId));
            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.getId());
            bookDTO.setTitle(book.getTitle());
            bookDTO.setAuthor(book.getAuthor());
            bookDTO.setGenre(book.getGenre());
            bookDTO.setSummary(book.getSummary());
            bookDTO.setImageURL(book.getBookImage());
            bookDTO.setAverage_rating(book.getAverage_rating());

            return bookDTO;
        } catch(NoSuchElementException e) {
            throw new NoSuchElementException("Book not found", e);
        }

    }
}
