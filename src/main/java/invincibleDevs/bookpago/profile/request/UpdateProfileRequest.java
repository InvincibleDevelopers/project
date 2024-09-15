package invincibleDevs.bookpago.profile.request;

import java.util.Optional;

public record UpdateProfileRequest(
        Optional<String> nickname,
        Optional<String> introduce
) {
}
