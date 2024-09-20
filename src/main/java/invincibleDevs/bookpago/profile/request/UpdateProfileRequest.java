package invincibleDevs.bookpago.profile.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public record UpdateProfileRequest(
        String username,
        Optional<String> nickname,
        Optional<String> introduce,
        Optional<MultipartFile> file
) {
}
