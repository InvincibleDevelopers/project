package invincibleDevs.bookpago.profile.response;

import invincibleDevs.bookpago.book.BookDTO;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.review.ReviewDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public record ProfileResponse(
        Long userId,
        String nickname,
        String introduce,
        String imageUrl,
        Optional<List<BookDTO>> wishBookDto,
        Optional<Page<ReadingClubDto>> readingClubDto, // Optional로 변경
        Optional<ReviewDto> reviewDto
) {

}
