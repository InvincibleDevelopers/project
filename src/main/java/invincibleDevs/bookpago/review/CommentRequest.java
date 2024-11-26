package invincibleDevs.bookpago.review;

import java.util.Optional;

public record CommentRequest(
        Long profileId,
        Optional<Long> reviewId,
        Optional<Long> commentId,
        Optional<String> content
) {

}
