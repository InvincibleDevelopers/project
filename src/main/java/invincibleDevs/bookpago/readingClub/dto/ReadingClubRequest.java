package invincibleDevs.bookpago.readingClub.dto;

import java.util.List;

public record ReadingClubRequest(
        Long kakaoId,
        String clubName,
        double latitude,
        double longitude,
        String address,
        String description,
        String time,
        int repeatCycle,
        List<Integer> weekDay
) {

}
