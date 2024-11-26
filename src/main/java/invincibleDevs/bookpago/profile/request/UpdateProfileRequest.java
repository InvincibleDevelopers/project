package invincibleDevs.bookpago.profile.request;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public record UpdateProfileRequest(
        Long kakaoId,
        Optional<String> nickname,
        Optional<String> introduce,
        Optional<MultipartFile> file
) {

}
