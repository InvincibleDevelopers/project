package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.service.ProfileService;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingClubFacade {

    private final ReadingClubService readingClubService;
    private final ReadingClubMapService readingClubMapService;
    private final ProfileService profileService;
    private final ReadingClubRepository readingClubRepository;

    public Page<ReadingClub> getClubs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReadingClub> readingClubsPage = readingClubRepository.findAll(pageable);

        return readingClubsPage;
    }

    public ReadingClubDto getClub(String nickname, Long clubId) {
        ReadingClub readingClub = readingClubService.findById(clubId);
        // Dto 변환 코드 (레코드 타입 사용)
        // ReadingClubMap의 갯수 구하기
        return new ReadingClubDto(
                readingClub.getClubMembers().size(),
                readingClub.getClubName(),
                readingClub.getLocation(),
                readingClub.getDescription(),
                readingClub.getMeetingTime()
        );
    }

    public ReadingClubDto createClub(ReadingClubRequest readingClubRequest) {
        Profile admin = profileService.findByNickname(readingClubRequest.nickname());
        // 새로운 ReadingClub을 생성하기 위해, clubMembers를 Set으로 만듦
        Set<ReadingClubMap> clubMembers = new HashSet<>();
        ReadingClub readingClub = readingClubService.createClub(readingClubRequest, clubMembers);
        clubMembers.add(
                readingClubMapService.create(admin, readingClub)); // 단일 ReadingClubMap 객체를 Set에 추가

        ReadingClubDto readingClubDto = new ReadingClubDto(clubMembers.size(),
                readingClubRequest.clubName(), readingClubRequest.location(),
                readingClubRequest.meetingTime(), readingClubRequest.description());

        return readingClubDto;
    }
}
