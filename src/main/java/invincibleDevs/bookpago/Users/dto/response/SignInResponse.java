package invincibleDevs.bookpago.Users.dto.response;

import invincibleDevs.bookpago.profile.model.Profile;

import java.util.Optional;

public record SignInResponse(
        boolean isUser,
        Optional<String> username,
        Optional<String> nickname,
        Optional<String> serverToken,
        String imageUrl


) {
}
