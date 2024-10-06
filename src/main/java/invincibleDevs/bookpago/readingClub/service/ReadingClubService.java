package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingClubService {

    private final ReadingClubRepository readingClubRepository;

    public ReadingClub findById(Long clubId) {
        return readingClubRepository.findById(clubId)
                                    .orElse(null);
    }

    public ReadingClub createClub(ReadingClubRequest readingClubRequest,
            Set<ReadingClubMap> members) {

        ReadingClub readingClub = ReadingClub.builder()
                                             .clubMembers(members)
                                             .clubName(readingClubRequest.clubName())
                                             .description(readingClubRequest.description())
                                             .location(readingClubRequest.location())
                                             .time(readingClubRequest.time())
                                             .repeatCycle(readingClubRequest.repeatCycle())
                                             .weekDay(readingClubRequest.weekDay())
                                             .build();
        return readingClubRepository.save(readingClub);
    }

    public int getMemberCount(ReadingClub readingClub) {
        // clubApplicant가 null인 경우만 멤버로 포함 (관리자와 정식 멤버)
        return (int) readingClub.getClubMembers().stream()
                .filter(clubMap -> clubMap.getClubApplicant() == null)
                .count();
    }
}
