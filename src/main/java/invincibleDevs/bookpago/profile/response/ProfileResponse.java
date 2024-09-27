package invincibleDevs.bookpago.profile.response;

import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public record ProfileResponse(
        Long userId,
        String nickname,
        String introduce,
        String imageUrl,

        List<Long> wishIsbnList,
        Optional<Page<ReadingClubDto>> readingClubDto // Optional로 변경
) {

}
