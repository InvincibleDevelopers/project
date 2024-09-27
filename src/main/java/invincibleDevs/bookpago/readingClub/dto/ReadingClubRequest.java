package invincibleDevs.bookpago.readingClub.dto;

public record ReadingClubRequest(
        String nickname,
        String clubName,
        String description,
        String location,
        String meetingTime
) {

}
