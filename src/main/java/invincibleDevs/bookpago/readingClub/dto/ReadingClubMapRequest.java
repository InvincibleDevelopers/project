package invincibleDevs.bookpago.readingClub.dto;

import java.util.List;

public record ReadingClubMapRequest(
        Long kakaoId,
        List<Long> members,
        List<Long> applicants

) {
}
