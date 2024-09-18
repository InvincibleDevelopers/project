package invincibleDevs.bookpago.book;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long isbn;
    private String title;
    private String author;
    private String image;
    private String description;
    private String publisher;
    private String pubdate;

}
