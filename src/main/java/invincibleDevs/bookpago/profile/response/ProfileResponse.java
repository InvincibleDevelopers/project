package invincibleDevs.bookpago.profile.response;

import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public record ProfileResponse(
        boolean isMyProfile,
        String nickname,
        String introduce,
        String imageUrl,

        List<Long> wishIsbnList,
        Optional<Page<ReadingClubDto>> readingClubDto // Optional로 변경
) {
}
