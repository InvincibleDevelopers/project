package invincibleDevs.bookpago.readingClub.dto;

import java.util.List;

public record ReadingClubRequest(
        Long id,
        Long kakaoId,
        String clubName,
        String location,
        String description,
        String time,
        int repeatCycle,
        List<Integer> weekDay
) {

}
