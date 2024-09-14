package invincibleDevs.bookpago.book;

import invincibleDevs.bookpago.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository <Book, Long> {
}
