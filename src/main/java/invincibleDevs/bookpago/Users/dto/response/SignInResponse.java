package invincibleDevs.bookpago.Users.dto.response;

import java.util.Optional;

public record SignInResponse(
        boolean isUser,
        Optional<String> username,
        Optional<String> nickname,
        Optional<String> serverToken

) {
}
