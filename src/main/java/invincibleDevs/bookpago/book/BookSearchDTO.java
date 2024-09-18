package invincibleDevs.bookpago.book;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookSearchDTO {
    private int total;
    private List<BookDTO> books;
}
