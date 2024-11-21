package invincibleDevs.bookpago.readingClub;

import invincibleDevs.bookpago.profile.ProfileDTO;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import java.util.List;

public record ClubWithMemberDto(
        ReadingClubDto readingClub,
        List<ProfileDTO> memberProfiles,
        boolean isAdmin

) {

}
