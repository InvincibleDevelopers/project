package invincibleDevs.bookpago.Users.dto.response;

import java.util.Optional;

public record SignInResponse(
        boolean isUser,
        Optional<Long> kakaoId,
        Optional<String> nickname,
//        Optional<String> serverToken,
        String imageUrl

) {

}
