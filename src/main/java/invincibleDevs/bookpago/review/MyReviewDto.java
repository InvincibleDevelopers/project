package invincibleDevs.bookpago.review;

public record MyReviewDto(
        String bookImageUrl,
        String bookTitle,
        String bookAuthor,
        Long id,
        String content,
        Long isbn,
        double rating,
        Long authorId
) {

}
