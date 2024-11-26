package invincibleDevs.bookpago.readingClub.dto;

import java.util.List;
import java.util.Optional;

public record ReadingClubMapRequest(
        Long kakaoId, //admin또는 멤버의 카카오 아이디
        Optional<List<Long>> members, // 이건 뭔지모르겠음. 모임 현재 멤버들?
        Optional<List<Long>> applicants //모임 신청자

) {

}
