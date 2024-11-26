package invincibleDevs.bookpago.book;

import lombok.*;

@Data
@AllArgsConstructor
public class BookDTO {
    private Long isbn;
    private String title;
    private String author;
    private String image;

}
