package invincibleDevs.bookpago.book;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDetailDTO {

    private boolean isWishBook;
    private Long isbn;
    private String title;
    private String author;
    private String image;
    private String description;
    private String publisher;
    private String pubdate;
    private float average_rating;
}
