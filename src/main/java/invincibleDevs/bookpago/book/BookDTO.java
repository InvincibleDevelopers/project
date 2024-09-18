package invincibleDevs.bookpago.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Long isbn;
    private String title;
    private String author;
    private String image;
    private String description;
    private String publisher;
    private Long pubdate;
}
