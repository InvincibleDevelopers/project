package invincibleDevs.bookpago.readingClub;

import invincibleDevs.bookpago.profile.ProfileDTO;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import java.util.List;

public record ClubWithMemberDto(
        boolean isAdmin,
        Long adminId,
        ReadingClubDto readingClub,
        List<ProfileDTO> memberList,
        List<ProfileDTO> applicantList

) {

}
