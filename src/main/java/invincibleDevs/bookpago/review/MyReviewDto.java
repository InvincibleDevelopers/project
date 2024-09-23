package invincibleDevs.bookpago.review;

import lombok.AllArgsConstructor;
import lombok.Data;


public record MyReviewDto(
        String bookImageUrl,
     String bookTitle,
     String bookAuthor,
     Long id,
     String content,
     Long isbn,
     int rating,
     Long authorId
) {
}
