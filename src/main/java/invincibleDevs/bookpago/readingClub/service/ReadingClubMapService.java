package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubMapRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingClubMapService {

    private final ReadingClubMapRepository readingClubMapRepository;
    private final ReadingClubService readingClubService;

    public Page<ReadingClubDto> getUserClubs(Profile profile, int page, int size) {
        // 관리자와 멤버로 검색하여 리스트 가져오기
        List<Long> clubIds = readingClubMapRepository.findDistinctReadingClubIdsByClubAdminOrClubMember(
                profile);
        System.out.println("%%%%%%%%%%%%%%");
        System.out.println(clubIds.size());
        // ReadingClubDto 리스트 초기화
        List<ReadingClubDto> readingClubDtoList = new ArrayList<>();
        for (Long clubId : clubIds) {
            ReadingClub readingClub = readingClubService.findById(clubId);
            ReadingClubDto readingClubDto = new ReadingClubDto(
                    readingClub.getId(),
                    readingClub.getClubMembers().size(),
                    readingClub.getClubName(),
                    readingClub.getLocation(),
                    readingClub.getDescription(),
                    readingClub.getTime(),
                    readingClub.getRepeatCycle(),
                    readingClub.getWeekDay()
            );
            readingClubDtoList.add(readingClubDto);

        }

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), readingClubDtoList.size());

        // 유효한 범위 내의 결과 리스트만 추출
        List<ReadingClubDto> content = (start > readingClubDtoList.size())
                ? new ArrayList<>()
                : readingClubDtoList.subList(start, end);

        // 페이징된 결과 리스트 반환
        return new PageImpl<>(content, pageable, readingClubDtoList.size());

    }

    public ReadingClubMap create_admin(Profile admin, ReadingClub readingClub) {
        ReadingClubMap readingClubMap = ReadingClubMap.builder()
                                                      .clubAdmin(admin)
                                                      .readingClub(readingClub)
                                                      .build();
        return readingClubMapRepository.save(readingClubMap);
    }

    public ReadingClubMap create_applicant(Profile applicant, ReadingClub readingClub) {
        ReadingClubMap readingClubMap = ReadingClubMap.builder()
                                                      .clubApplicant(applicant)
                                                      .readingClub(readingClub)
                                                      .build();
        return readingClubMapRepository.save(readingClubMap);
    }

    public ReadingClubMap create_member(Profile member, ReadingClub readingClub) {
        ReadingClubMap readingClubMap = ReadingClubMap.builder()
                                                      .clubMember(member)
                                                      .readingClub(readingClub)
                                                      .build();
        return readingClubMapRepository.save(readingClubMap);
    }

    public ReadingClubMap delete(Profile admin, ReadingClub readingClub) {
        ReadingClubMap readingClubMap = ReadingClubMap.builder()
                                                      .clubAdmin(admin)
                                                      .readingClub(readingClub)
                                                      .build();
        return readingClubMapRepository.save(readingClubMap);
    }
}
