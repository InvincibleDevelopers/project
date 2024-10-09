package invincibleDevs.bookpago.review;

import java.util.Optional;

public record ReviewRequest(
        Optional<Long> reviewId,

        Long isbn,
        double rating,
        String content,
        Long kakaoId
) {

}
