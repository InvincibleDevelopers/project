package invincibleDevs.bookpago.book;

import invincibleDevs.bookpago.review.ReviewDto;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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

    // 리뷰 리스트를 Optional로 감싸서 처리
    private Optional<Page<ReviewDto>> reviews = Optional.empty(); // 초기값으로 빈 Optional 설정

}
