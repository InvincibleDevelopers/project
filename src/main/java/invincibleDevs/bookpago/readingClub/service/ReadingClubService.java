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
                                             .id(readingClubRequest.id())
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
}
