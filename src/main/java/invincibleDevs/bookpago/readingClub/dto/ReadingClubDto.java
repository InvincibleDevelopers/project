package invincibleDevs.bookpago.readingClub.dto;

public record ReadingClubDto(
        int members,
        String clubName,
        String location,
        String meetingTime,
        String description
) {

}
