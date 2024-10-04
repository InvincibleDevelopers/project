package invincibleDevs.bookpago.readingClub.dto;

import java.util.List;

public record ReadingClubDto(
        Long id,
        int members,
        String clubName,
        String location,
        String description,
        String time,
        int repeatCycle,
        List<Integer> weekDay
) {

}
