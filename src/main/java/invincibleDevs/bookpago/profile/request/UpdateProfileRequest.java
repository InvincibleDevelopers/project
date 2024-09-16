package invincibleDevs.bookpago.profile.request;

import java.util.Optional;

public record UpdateProfileRequest(
        String username,
        Optional<String> nickname,
        Optional<String> introduce
) {
}
