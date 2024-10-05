package invincibleDevs.bookpago.review;

public record ReviewDto(
        Long id,
        double rating,
        String content,
        String nickname
) {

}
