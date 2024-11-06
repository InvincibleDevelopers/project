package invincibleDevs.bookpago.readingClub.dto;

import invincibleDevs.bookpago.readingClub.ProfileDTO;

import java.util.List;

public record ReadingClubDetailDto(
        Long id,
        Long adminId,
        int memberCount,
        String clubName,
        String location,
        String description,
        String time,
        int repeatCycle,
        List<Integer> weekDay,
        List<ProfileDTO> memberList,
        List<ProfileDTO> applicantList
) {
}
