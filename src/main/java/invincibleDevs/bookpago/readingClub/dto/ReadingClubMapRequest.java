package invincibleDevs.bookpago.readingClub.dto;

import java.util.List;

public record ReadingClubMapRequest(
        Long kakaoId, //admin의 카카오 아이디
        List<Long> members, // 이건 뭔지모르겠음. 모임 현재 멤버들?
        List<Long> applicants //모임 신청자

) {

}
