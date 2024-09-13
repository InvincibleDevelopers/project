package invincibleDevs.bookpago.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String summary;
    private String imageURL;
    private Long average_rating;
}
