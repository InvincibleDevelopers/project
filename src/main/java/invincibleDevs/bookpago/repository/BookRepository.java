package invincibleDevs.bookpago.repository;

import invincibleDevs.bookpago.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository <Book, Long> {
}
