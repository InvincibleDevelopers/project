package invincibleDevs.bookpago.profile.response;

import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public record ProfileResponse(
        boolean isMyProfile,
        String nickname,
        String introduce,
        String imageUrl,
        Optional<Page<ReadingClubDto>> readingClubDto // Optional로 변경
) {
}
