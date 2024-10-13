package invincibleDevs.bookpago.review;

import java.util.Optional;

public record ReviewDto(
        Long id,
        double rating,
        String content,
        String nickname,
        String profileImage,
        Optional<Boolean> isLiked,
        int likes

) {

}
